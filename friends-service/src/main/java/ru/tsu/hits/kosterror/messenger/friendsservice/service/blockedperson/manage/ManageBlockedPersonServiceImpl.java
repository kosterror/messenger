package ru.tsu.hits.kosterror.messenger.friendsservice.service.blockedperson.manage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import ru.tsu.hits.kosterror.messenger.core.dto.NewNotificationDto;
import ru.tsu.hits.kosterror.messenger.core.dto.PersonDto;
import ru.tsu.hits.kosterror.messenger.core.enumeration.NotificationType;
import ru.tsu.hits.kosterror.messenger.core.exception.BadRequestException;
import ru.tsu.hits.kosterror.messenger.core.exception.InternalException;
import ru.tsu.hits.kosterror.messenger.core.exception.NotFoundException;
import ru.tsu.hits.kosterror.messenger.core.integration.auth.personinfo.IntegrationPersonInfoService;
import ru.tsu.hits.kosterror.messenger.core.util.RabbitMQBindings;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.BlockedPersonDto;
import ru.tsu.hits.kosterror.messenger.friendsservice.entity.BlockedPerson;
import ru.tsu.hits.kosterror.messenger.friendsservice.mapper.BlockedPersonMapper;
import ru.tsu.hits.kosterror.messenger.friendsservice.repository.BlockedPersonRepository;
import ru.tsu.hits.kosterror.messenger.friendsservice.service.friend.manage.ManageFriendService;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

/**
 * Класс, реализующий интерфейс {@link ManageBlockedPersonService}
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ManageBlockedPersonServiceImpl implements ManageBlockedPersonService {

    private final BlockedPersonRepository blockedPersonRepository;
    private final ManageFriendService manageFriendService;
    private final BlockedPersonMapper blockedPersonMapper;
    private final IntegrationPersonInfoService integrationPersonInfoService;
    private final StreamBridge streamBridge;

    @Override
    @Transactional
    public BlockedPersonDto createBlockedPerson(UUID ownerId, UUID memberId) {
        if (ownerId.equals(memberId)) {
            throw new BadRequestException("Нельзя добавтиь самого себя в черный список");
        }

        PersonDto personDetails = personDto(memberId);

        if (manageFriendService.isFriends(ownerId, memberId)) {
            manageFriendService.deleteFriend(ownerId, memberId);
        }

        BlockedPerson blockedPerson;
        Optional<BlockedPerson> blockedPersonOptional = blockedPersonRepository
                .findBlockedPersonByOwnerIdAndMemberId(ownerId, memberId);

        if (blockedPersonOptional.isPresent()) {
            blockedPerson = blockedPersonOptional.get();
            if (Boolean.TRUE.equals(blockedPerson.getIsDeleted())) {
                makeEntityBlocked(blockedPerson, personDetails);
            } else {
                throw new BadRequestException("Пользователь уже заблокирован");
            }
        } else {
            blockedPerson = new BlockedPerson();
            blockedPerson.setOwnerId(ownerId);
            makeEntityBlocked(blockedPerson, personDetails);
        }

        blockedPerson = blockedPersonRepository.save(blockedPerson);
        sendNotificationAddedToBlockedList(ownerId, memberId);
        return blockedPersonMapper.entityToDto(blockedPerson);
    }

    @Override
    public void deleteBlockedPerson(UUID ownerId, UUID memberId) {
        if (ownerId.equals(memberId)) {
            throw new BadRequestException("Некорректный запрос. Идентификаторы совпадают.");
        }
        BlockedPerson blockedPerson = blockedPersonRepository
                .findBlockedPersonByOwnerIdAndMemberIdAndIsDeleted(ownerId, memberId, false)
                .orElseThrow(() -> new BadRequestException(String.format("Пользователь с id = '%s' не" +
                        " заблокировал пользователя с id = '%s'", ownerId, memberId)));

        blockedPerson.setIsDeleted(true);
        blockedPerson.setDeleteDate(LocalDate.now());
        blockedPersonRepository.save(blockedPerson);
        sendNotificationRemovedFromBlockedList(ownerId, memberId);
    }

    /**
     * Метод, чтобы сделать сущность {@link BlockedPerson} активированной.
     *
     * @param entity сущность.
     * @param dto    информация о заблокированном пользователе.
     */
    private void makeEntityBlocked(BlockedPerson entity, PersonDto dto) {
        entity.setMemberId(dto.getId());
        entity.setMemberFullName(dto.getFullName());
        entity.setAddedDate(LocalDate.now());
        entity.setIsDeleted(false);
        entity.setDeleteDate(null);
    }

    private PersonDto personDto(UUID personId) {
        try {
            return integrationPersonInfoService.getPersonInfo(personId);
        } catch (HttpClientErrorException.NotFound exception) {
            throw new NotFoundException(String.format("Пользователь с id %s не найден", personId));
        } catch (RestClientException exception) {
            throw new InternalException("Ошибка во время интеграционного запроса в auth-service", exception);
        }
    }

    private void sendNotificationAddedToBlockedList(UUID authorId, UUID receiverId) {
        PersonDto authorDetails = personDto(authorId);
        String notificationText = String.format("Пользователь %s добавил вас в черный список",
                authorDetails.getFullName()
        );

        sendNotification(receiverId, NotificationType.ADDED_TO_BLOCKED_LIST, notificationText);
    }

    private void sendNotificationRemovedFromBlockedList(UUID authorId, UUID receiverId) {
        PersonDto authorDetails = personDto(authorId);
        String notificationText = String.format("Пользователь %s удалил вас в из черного списка",
                authorDetails.getFullName()
        );

        sendNotification(receiverId, NotificationType.REMOVED_FROM_BLOCKED_LIST, notificationText);
    }

    private void sendNotification(UUID receiverId, NotificationType notificationType, String notificationText) {
        NewNotificationDto notification = new NewNotificationDto(receiverId, notificationType, notificationText);
        streamBridge.send(RabbitMQBindings.CREATE_NOTIFICATION_OUT, notification);
    }

}
