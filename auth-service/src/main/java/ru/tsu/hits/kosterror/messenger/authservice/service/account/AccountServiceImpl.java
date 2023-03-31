package ru.tsu.hits.kosterror.messenger.authservice.service.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.PersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.UpdatePersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.entity.Person;
import ru.tsu.hits.kosterror.messenger.authservice.exception.NotFoundException;
import ru.tsu.hits.kosterror.messenger.authservice.mapper.PersonMapper;
import ru.tsu.hits.kosterror.messenger.authservice.repository.PersonRepository;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final PersonRepository repository;

    private final PersonMapper mapper;

    @Override
    public PersonDto getAccountInfo(String login) throws NotFoundException {
        Person person = repository
                .findByLogin(login)
                .orElseThrow(() -> new NotFoundException("Пользователь с login = " + login + "не найден"));

        return mapper.entityToDto(person);
    }

    @Override
    public PersonDto updateAccount(String login, UpdatePersonDto dto) throws NotFoundException {
        Person person = repository
                .findByLogin(login)
                .orElseThrow(() -> new NotFoundException("Пользователь с login = " + login + "не найден"));

        person.setPassword(dto.getPassword());
        person.setEmail(dto.getEmail());
        person.setName(dto.getName());
        person.setSurname(dto.getSurname());
        person.setBirthDate(dto.getBirthDate());
        person.setGender(dto.getGender());

        if (dto.getPatronymic() != null && !dto.getPatronymic().isBlank()) {
            person.setPatronymic(dto.getPatronymic());
        }

        person = repository.save(person);

        return mapper.entityToDto(person);
    }
}
