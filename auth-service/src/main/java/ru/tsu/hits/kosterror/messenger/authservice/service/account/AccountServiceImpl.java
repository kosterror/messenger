package ru.tsu.hits.kosterror.messenger.authservice.service.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.PersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.UpdatePersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.entity.Person;
import ru.tsu.hits.kosterror.messenger.authservice.exception.NotFoundException;
import ru.tsu.hits.kosterror.messenger.authservice.mapper.PersonMapper;
import ru.tsu.hits.kosterror.messenger.authservice.repository.PersonRepository;

/**
 * Класс реализующий интерфейс {@link AccountService}.
 */
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private static final String ACCOUNT_NOT_FOUND_MESSAGE = "Пользователь с логином = '%s' не найден";
    private final PersonRepository repository;

    private final PersonMapper mapper;

    @Override
    public PersonDto getAccountInfo(String login) throws NotFoundException {
        Person person = findPerson(login);

        return mapper.entityToDto(person);
    }

    @Override
    public PersonDto updateAccount(String login, UpdatePersonDto dto) throws NotFoundException {
        Person person = findPerson(login);

        person.setFullName(dto.getFullName());
        person.setBirthDate(dto.getBirthDate());
        person.setPhoneNumber(dto.getPhoneNumber());
        person.setCity(dto.getCity());
        person.setAvatarId(dto.getAvatarId());
        person = repository.save(person);

        return mapper.entityToDto(person);
    }

    private Person findPerson(String login) throws NotFoundException {
        return repository
                .findByLogin(login)
                .orElseThrow(() -> new NotFoundException(String.format(ACCOUNT_NOT_FOUND_MESSAGE, login)));
    }

}
