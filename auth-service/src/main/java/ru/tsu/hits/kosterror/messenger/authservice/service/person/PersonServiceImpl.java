package ru.tsu.hits.kosterror.messenger.authservice.service.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.PersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.UpdatePersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.request.PersonPageRequest;
import ru.tsu.hits.kosterror.messenger.authservice.entity.Person;
import ru.tsu.hits.kosterror.messenger.authservice.mapper.PersonMapper;
import ru.tsu.hits.kosterror.messenger.authservice.repository.PersonRepository;
import ru.tsu.hits.kosterror.messenger.authservice.service.httpsender.HttpSenderService;
import ru.tsu.hits.kosterror.messenger.core.dto.BooleanDto;
import ru.tsu.hits.kosterror.messenger.core.dto.PairPersonIdDto;
import ru.tsu.hits.kosterror.messenger.core.exception.BadRequestException;
import ru.tsu.hits.kosterror.messenger.core.exception.ForbiddenException;
import ru.tsu.hits.kosterror.messenger.core.exception.InternalException;
import ru.tsu.hits.kosterror.messenger.core.exception.NotFoundException;
import ru.tsu.hits.kosterror.messenger.core.response.ApiError;
import ru.tsu.hits.kosterror.messenger.core.response.PagingParamsResponse;
import ru.tsu.hits.kosterror.messenger.core.response.PagingResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Класс реализующий интерфейс {@link PersonService}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PersonServiceImpl implements PersonService {

    private static final String PERSON_NOT_FOUND = "Пользователь с логином = '%s' не найден";
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    private final HttpSenderService httpSenderService;
    private final ObjectMapper objectMapper;

    @Override
    public PersonDto getPersonInfo(UUID personId) {
        Person person = personRepository
                .findById(personId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        return personMapper.entityToDto(person);
    }

    @Override
    public PersonDto getMyPersonInfo(String login) throws NotFoundException {
        Person person = findPerson(login);

        return personMapper.entityToDto(person);
    }

    @Override
    public PersonDto updatePersonInfo(String login, UpdatePersonDto dto) throws NotFoundException {
        Person person = findPerson(login);

        person.setFullName(dto.getFullName());
        person.setBirthDate(dto.getBirthDate());
        person.setPhoneNumber(dto.getPhoneNumber());
        person.setCity(dto.getCity());
        person.setAvatarId(dto.getAvatarId());
        person = personRepository.save(person);

        return personMapper.entityToDto(person);
    }

    @Override
    public PagingResponse<List<PersonDto>> getPersons(PersonPageRequest personPageRequest) {
        try {
            Pageable pageable = buildPageable(personPageRequest);
            Page<Person> personPage;

            if (personPageRequest.getPersonFilters() != null) {
                ExampleMatcher exampleMatcher = buildExampleMatcher();
                Example<Person> personExample = Example.of(
                        personMapper.filtersToEntity(personPageRequest.getPersonFilters()),
                        exampleMatcher
                );

                personPage = personRepository.findAll(personExample, pageable);
            } else {
                personPage = personRepository.findAll(pageable);
            }

            List<PersonDto> personDtos = personPage
                    .getContent()
                    .stream()
                    .map(personMapper::entityToDto)
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

    @Override
    public PersonDto getPersonInfo(String askerPersonLogin, String askedPersonLogin) {
        if (askerPersonLogin.equals(askedPersonLogin)) {
            log.error("Логины совпали: {} = {}", askerPersonLogin, askedPersonLogin);
            throw new BadRequestException("Некорректный запрос. Логин пользователя, который запрашивает и того," +
                    "чью информацию о профиле запрашивают, равны");
        }

        Person askerPerson = findPerson(askerPersonLogin);
        Person askedPerson = findPerson(askedPersonLogin);
        UUID ownerUUID = askedPerson.getId();
        UUID memberUUID = askerPerson.getId();
        PairPersonIdDto requestBody = new PairPersonIdDto(ownerUUID, memberUUID);

        var responseEntity = httpSenderService.friendsServicePersonIsBlocked(requestBody);
        Object responseBody = responseEntity.getBody();

        if (responseBody != null) {
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                BooleanDto convertedBody = objectMapper.convertValue(responseBody, BooleanDto.class);
                if (!convertedBody.isValue()) {
                    return personMapper.entityToDto(askedPerson);
                } else {
                    throw new ForbiddenException("Пользователь добавил вас в черный список");
                }
            }
            ApiError apiErrorBody = objectMapper.convertValue(responseBody, ApiError.class);
            log.error("Ошибка при интеграционном запросе. Статус код: '{}'. Текст ошибки: '{}'",
                    apiErrorBody.getStatus(), apiErrorBody.getMessage());
            throw new InternalException(String.format("Ошибка при интеграционном запросе в friends-service, " +
                    "на получение информации находится ли пользователь с логином %s в черном списке" +
                    "у пользователя с логином %s", askerPersonLogin, askedPersonLogin));
        } else {
            throw new InternalException("Ошибка при интеграционном запросе. Тело ответа: null");
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
        return personRepository
                .findByLogin(login)
                .orElseThrow(() -> new NotFoundException(String.format(PERSON_NOT_FOUND, login)));
    }

}
