package ru.tsu.hits.kosterror.messenger.friendsservice.service.friend.manage;

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
import ru.tsu.hits.kosterror.messenger.core.exception.ForbiddenException;
import ru.tsu.hits.kosterror.messenger.core.exception.InternalException;
import ru.tsu.hits.kosterror.messenger.core.exception.NotFoundException;
import ru.tsu.hits.kosterror.messenger.core.integration.auth.personinfo.IntegrationPersonInfoService;
import ru.tsu.hits.kosterror.messenger.core.util.RabbitMQBindings;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.FriendDto;
import ru.tsu.hits.kosterror.messenger.friendsservice.entity.Friend;
import ru.tsu.hits.kosterror.messenger.friendsservice.mapper.FriendMapper;
import ru.tsu.hits.kosterror.messenger.friendsservice.repository.FriendRepository;
import ru.tsu.hits.kosterror.messenger.friendsservice.service.blockedperson.display.DisplayBlockedPersonService;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

/**
 * Класс, реализующий интерфейс {@link ManageFriendService}
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ManageFriendServiceImpl implements ManageFriendService {

    private static final String ADDED_TO_FRIEND_LIST_NOTIFICATION_TEXT = "Пользователь %s добавил вас в друзья";
    private static final String REMOVED_FROM_FRIEND_LIST_NOTIFICATION_TEXT = "Пользователь %s удалил вас из друзей";
    private static final String FRIEND_RELATION_ONE_DIRECTION = "Нарушена целостность данных. Связь дружбы является" +
            " односторонней для пользователей: '%s', '%s'";
    private final FriendRepository friendRepository;
    private final FriendMapper friendMapper;
    private final DisplayBlockedPersonService displayBlockedPersonService;
    private final IntegrationPersonInfoService integrationPersonInfoService;
    private final StreamBridge streamBridge;

    @Override
    @Transactional
    public FriendDto createFriend(UUID ownerId, UUID memberId) {
        validateIds(ownerId, memberId);

        PersonDto ownerDetails = personDto(ownerId);
        PersonDto memberDetails = personDto(memberId);

        Optional<Friend> ownerFriendOptional = friendRepository.findFriendByOwnerIdAndMemberId(ownerId, memberId);
        Optional<Friend> memberFriendOptional = friendRepository.findFriendByOwnerIdAndMemberId(memberId, ownerId);

        Friend ownerFriend;
        Friend memberFriend;

        if (ownerFriendOptional.isPresent() && memberFriendOptional.isPresent()) {
            ownerFriend = ownerFriendOptional.get();
            memberFriend = memberFriendOptional.get();

            if (ownerFriend.getIsDeleted().equals(memberFriend.getIsDeleted())) {
                if (Boolean.TRUE.equals(ownerFriend.getIsDeleted())) {
                    ownerFriend.setIsDeleted(false);
                    ownerFriend.setDeletedDate(null);
                    ownerFriend.setAddedDate(LocalDate.now());

                    memberFriend.setIsDeleted(false);
                    memberFriend.setDeletedDate(null);
                    memberFriend.setAddedDate(LocalDate.now());
                } else {
                    throw new BadRequestException("Вы уже являетесь друзьями");
                }
            } else {
                throw new InternalException(String.format(FRIEND_RELATION_ONE_DIRECTION, ownerId, memberId));
            }
        } else if (ownerFriendOptional.isEmpty() && memberFriendOptional.isEmpty()) {
            ownerFriend = buildFriend(ownerId, memberDetails);
            memberFriend = buildFriend(memberId, ownerDetails);
        } else {
            throw new InternalException(String.format("Аномалия данных. Существует однонаправленная" +
                    " связь дружбы у: %s %s", ownerId, memberId));
        }

        ownerFriend = friendRepository.save(ownerFriend);
        memberFriend = friendRepository.save(memberFriend);

        sendAddedToFriendListNotification(memberFriend.getMemberFullName(), memberFriend.getOwnerId());

        return friendMapper.entityToDto(ownerFriend);
    }

    @Override
    @Transactional
    public void deleteFriend(UUID ownerId, UUID memberId) {
        if (ownerId.equals(memberId)) {
            throw new BadRequestException("Некорректный запрос. Идентификаторы совпадают.");
        }

        Optional<Friend> optionalOwnerFriend = friendRepository.findFriendByOwnerIdAndMemberId(ownerId, memberId);
        Optional<Friend> optionalMemberFriend = friendRepository.findFriendByOwnerIdAndMemberId(memberId, ownerId);
        Friend ownerFriend;
        Friend memberFriend;

        if (optionalOwnerFriend.isPresent() && optionalMemberFriend.isPresent()) {
            ownerFriend = optionalOwnerFriend.get();
            memberFriend = optionalMemberFriend.get();

            if (Boolean.FALSE.equals(ownerFriend.getIsDeleted())
                    && Boolean.FALSE.equals(memberFriend.getIsDeleted())) {
                makeFriendDeleted(ownerFriend);
                makeFriendDeleted(memberFriend);
            } else if (ownerFriend.getIsDeleted() && memberFriend.getIsDeleted()) {
                throw new BadRequestException("Вы не являетесь друзьями");
            } else {
                throw new InternalException(
                        String.format(
                                FRIEND_RELATION_ONE_DIRECTION,
                                ownerFriend.getOwnerId(),
                                memberFriend.getOwnerId()
                        )
                );
            }
        } else if (optionalOwnerFriend.isEmpty() && optionalMemberFriend.isEmpty()) {
            throw new BadRequestException("Вы не являетесь друзьями");
        } else {
            throw new InternalException(
                    String.format(
                            FRIEND_RELATION_ONE_DIRECTION,
                            ownerId,
                            memberId
                    )
            );
        }

        friendRepository.save(ownerFriend);
        memberFriend = friendRepository.save(memberFriend);

        sendRemovedFromFriendListNotification(memberFriend.getMemberFullName(), memberFriend.getOwnerId());
    }

    @Override
    public boolean isFriends(UUID firstId, UUID secondId) {
        return friendRepository.existsByOwnerIdAndMemberIdAndIsDeleted(firstId, secondId, false)
                || friendRepository.existsByOwnerIdAndMemberIdAndIsDeleted(secondId, firstId, false);
    }

    private void validateIds(UUID ownerId, UUID memberId) {
        if (ownerId.equals(memberId)) {
            throw new BadRequestException("Идентификаторы совпадают, нельзя добавить себя в друзья");
        }

        if (Boolean.TRUE.equals(displayBlockedPersonService.personIsBlocked(ownerId, memberId).getValue())) {
            throw new BadRequestException("Нельзя добавить пользователя, который находится в черном списке");
        }

        if (Boolean.TRUE.equals(displayBlockedPersonService.personIsBlocked(memberId, ownerId).getValue())) {
            throw new ForbiddenException("Пользователь добавил вас в черный список");
        }
    }

    /**
     * Метод для назначения параметров для сущности друга. <strong>Изменяет входящий объект</strong>.
     *
     * @param friend сущность друга.
     */
    private void makeFriendDeleted(Friend friend) {
        friend.setIsDeleted(true);
        friend.setDeletedDate(LocalDate.now());
    }

    private Friend buildFriend(UUID ownerId, PersonDto memberDetails) {
        return Friend
                .builder()
                .ownerId(ownerId)
                .memberId(memberDetails.getId())
                .memberFullName(memberDetails.getFullName())
                .deletedDate(null)
                .isDeleted(false)
                .addedDate(LocalDate.now())
                .build();
    }

    private void sendAddedToFriendListNotification(String authorFullName, UUID receiverId) {
        NewNotificationDto newNotificationDto = new NewNotificationDto(receiverId,
                NotificationType.ADDED_TO_FRIEND_LIST,
                String.format(ADDED_TO_FRIEND_LIST_NOTIFICATION_TEXT, authorFullName)
        );

        streamBridge.send(RabbitMQBindings.CREATE_NOTIFICATION_OUT, newNotificationDto);
    }

    private void sendRemovedFromFriendListNotification(String authorFullName, UUID receiverId) {
        NewNotificationDto newNotificationDto = new NewNotificationDto(receiverId,
                NotificationType.REMOVED_FROM_FRIEND_LIST,
                String.format(REMOVED_FROM_FRIEND_LIST_NOTIFICATION_TEXT, authorFullName)
        );

        streamBridge.send(RabbitMQBindings.CREATE_NOTIFICATION_OUT, newNotificationDto);
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

}
