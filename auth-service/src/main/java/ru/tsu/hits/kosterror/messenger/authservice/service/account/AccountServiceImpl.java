package ru.tsu.hits.kosterror.messenger.authservice.service.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.PersonDto;
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

}
