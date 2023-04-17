package ru.tsu.hits.kosterror.messenger.friendsservice.service.blockedperson.synchronize;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.messenger.core.dto.PersonDto;
import ru.tsu.hits.kosterror.messenger.core.exception.BadRequestException;
import ru.tsu.hits.kosterror.messenger.core.exception.InternalException;
import ru.tsu.hits.kosterror.messenger.core.response.ApiError;
import ru.tsu.hits.kosterror.messenger.friendsservice.entity.BlockedPerson;
import ru.tsu.hits.kosterror.messenger.friendsservice.repository.BlockedPersonRepository;
import ru.tsu.hits.kosterror.messenger.friendsservice.service.integration.authservice.AuthIntegrationService;

import java.util.List;
import java.util.UUID;

/**
 * Класс, реализующий интерфейс {@link  SynchronizeBlockedPersonsService}.
 */
@Service
@RequiredArgsConstructor
public class SynchronizeBlockedPersonsServiceImpl implements SynchronizeBlockedPersonsService {

    private final BlockedPersonRepository blockedPersonRepository;
    private final AuthIntegrationService authIntegrationService;
    private final ObjectMapper objectMapper;

    @Override
    public void syncBlockedPersonIdFullName(UUID blockedPersonId) {
        ResponseEntity<Object> response = authIntegrationService.getPersonInfo(blockedPersonId);
        if (response.getStatusCode() == HttpStatus.OK) {
            PersonDto person = objectMapper.convertValue(response.getBody(), PersonDto.class);
            List<BlockedPerson> blockedPersons = blockedPersonRepository.findAllByMemberId(person.getId());
            blockedPersons.forEach(blockedPerson -> blockedPerson.setMemberFullName(person.getFullName()));

            blockedPersonRepository.saveAll(blockedPersons);
        } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new BadRequestException(
                    String.format("Пользователь с идентификатором '%s' не существует", blockedPersonRepository)
            );
        } else {
            ApiError error = objectMapper.convertValue(response.getBody(), ApiError.class);
            throw new InternalException(String.format("Ошибка во время выполнения интеграционного запроса:" +
                    "текст ошибки: '%s', статус код: '%s'", error.getMessage(), error.getStatus()));
        }
    }

}
