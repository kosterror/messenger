package ru.tsu.hits.kosterror.messenger.friendsservice.service.blockedperson.synchronize;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import ru.tsu.hits.kosterror.messenger.core.dto.PersonDto;
import ru.tsu.hits.kosterror.messenger.core.exception.InternalException;
import ru.tsu.hits.kosterror.messenger.core.exception.NotFoundException;
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
@Slf4j
public class SynchronizeBlockedPersonsServiceImpl implements SynchronizeBlockedPersonsService {

    private final BlockedPersonRepository blockedPersonRepository;
    private final AuthIntegrationService authIntegrationService;

    @Override
    public void syncBlockedPersonIdFullName(UUID blockedPersonId) {
        try {
            PersonDto personDto = authIntegrationService.getPersonInfo(blockedPersonId);
            List<BlockedPerson> blockedPersons = blockedPersonRepository.findAllByMemberId(personDto.getId());
            blockedPersons.forEach(blockedPerson -> blockedPerson.setMemberFullName(personDto.getFullName()));

            blockedPersonRepository.saveAll(blockedPersons);
        } catch (HttpClientErrorException.NotFound exception) {
            throw new NotFoundException("Пользователь не найден");
        } catch (Exception exception) {
            throw new InternalException("Исключение во время выполнения интеграционного запроса", exception);
        }
    }

}
