package ru.tsu.hits.kosterror.messenger.authservice.service.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.PersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.UpdatePersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.request.PersonPageRequest;
import ru.tsu.hits.kosterror.messenger.authservice.entity.Person;
import ru.tsu.hits.kosterror.messenger.authservice.mapper.PersonMapper;
import ru.tsu.hits.kosterror.messenger.authservice.repository.PersonRepository;
import ru.tsu.hits.kosterror.messenger.core.exception.BadRequestException;
import ru.tsu.hits.kosterror.messenger.core.exception.NotFoundException;
import ru.tsu.hits.kosterror.messenger.core.response.PagingParamsResponse;
import ru.tsu.hits.kosterror.messenger.core.response.PagingResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс реализующий интерфейс {@link PersonService}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PersonServiceImpl implements PersonService {

    private static final String ACCOUNT_NOT_FOUND_MESSAGE = "Пользователь с логином = '%s' не найден";
    private final PersonRepository repository;

    private final PersonMapper mapper;

    @Override
    public PersonDto getMyPersonInfo(String login) throws NotFoundException {
        Person person = findPerson(login);

        return mapper.entityToDto(person);
    }

    @Override
    public PersonDto updatePersonInfo(String login, UpdatePersonDto dto) throws NotFoundException {
        Person person = findPerson(login);

        person.setFullName(dto.getFullName());
        person.setBirthDate(dto.getBirthDate());
        person.setPhoneNumber(dto.getPhoneNumber());
        person.setCity(dto.getCity());
        person.setAvatarId(dto.getAvatarId());
        person = repository.save(person);

        return mapper.entityToDto(person);
    }

    @Override
    public PagingResponse<List<PersonDto>> getPersons(PersonPageRequest personPageRequest) {
        try {
            Pageable pageable = buildPageable(personPageRequest);
            Page<Person> personPage;

            if (personPageRequest.getPersonFilters() != null) {
                ExampleMatcher exampleMatcher = buildExampleMatcher();
                Example<Person> personExample = Example.of(
                        mapper.filtersToEntity(personPageRequest.getPersonFilters()),
                        exampleMatcher
                );

                personPage = repository.findAll(personExample, pageable);
            } else {
                personPage = repository.findAll(pageable);
            }

            List<PersonDto> personDtos = personPage
                    .getContent()
                    .stream()
                    .map(mapper::entityToDto)
                    .collect(Collectors.toList());

            PagingParamsResponse pagingParams = new PagingParamsResponse(
                    personPage.getTotalPages(),
                    personPage.getTotalElements(),
                    personPage.getNumber(),
                    personDtos.size()
            );

            return new PagingResponse<>(pagingParams, personDtos);
        } catch (PropertyReferenceException propertyReferenceException) {
            log.error("Некорректные параметры для фильтрации или сортировки {}",
                    propertyReferenceException.getMessage(),
                    propertyReferenceException);
            throw new BadRequestException(String.format("Некорректное свойство для фильтрации или " +
                            "сортировки %s",
                    propertyReferenceException.getPropertyName())
            );
        }
    }

    private ExampleMatcher buildExampleMatcher() {
        return ExampleMatcher
                .matchingAll()
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    }

    private Pageable buildPageable(PersonPageRequest personPageRequest) {
        int page = personPageRequest.getPagingParams().getPage();
        int size = personPageRequest.getPagingParams().getSize();

        PageRequest pageRequest;

        Map<String, Sort.Direction> sortProperties = personPageRequest.getSortProperties();
        if (sortProperties != null && sortProperties.size() > 0) {
            List<Sort.Order> orders = new ArrayList<>();

            for (Map.Entry<String, Sort.Direction> entry : sortProperties.entrySet()) {
                Sort.Direction direction = entry.getValue();
                String property = entry.getKey();

                Sort.Order order = direction == Sort.Direction.ASC ?
                        Sort.Order.asc(property) : Sort.Order.desc(property);

                orders.add(order);
            }

            Sort sort = Sort.by(orders);
            pageRequest = PageRequest.of(page, size, sort);

        } else {
            pageRequest = PageRequest.of(page, size);
        }

        return pageRequest;
    }

    private Person findPerson(String login) throws NotFoundException {
        return repository
                .findByLogin(login)
                .orElseThrow(() -> new NotFoundException(String.format(ACCOUNT_NOT_FOUND_MESSAGE, login)));
    }

}
