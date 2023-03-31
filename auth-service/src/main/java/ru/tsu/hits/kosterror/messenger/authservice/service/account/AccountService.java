package ru.tsu.hits.kosterror.messenger.authservice.service.account;

import ru.tsu.hits.kosterror.messenger.authservice.dto.person.PersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.exception.NotFoundException;

public interface AccountService {

    PersonDto getAccountInfo(String login) throws NotFoundException;

}
