package ru.tsu.hits.kosterror.messenger.friendsservice.service.friend.manage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.kosterror.messenger.core.exception.BadRequestException;
import ru.tsu.hits.kosterror.messenger.friendsservice.entity.Friend;
import ru.tsu.hits.kosterror.messenger.friendsservice.repository.FriendRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Класс, реализующий интерфейс {@link FriendManageService}
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FriendManageServiceImpl implements FriendManageService {

    private final FriendRepository friendRepository;

    @Override
    @Transactional
    public void deleteFriend(UUID ownerId, UUID memberId) {
        if (ownerId == memberId) {
            throw new BadRequestException("Некорректный запрос. Идентификаторы совпадают.");
        }

        Optional<Friend> optionalOwnerFriend = friendRepository.findFriendByOwnerIdAndMemberId(ownerId, memberId);
        Optional<Friend> optionalMemberFriend = friendRepository.findFriendByOwnerIdAndMemberId(memberId, ownerId);

        if (optionalOwnerFriend.isPresent() && optionalMemberFriend.isPresent()) {
            Friend ownerFriend = makeFriendDeleted(optionalOwnerFriend.get());
            Friend memberFriend = makeFriendDeleted(optionalMemberFriend.get());
            friendRepository.save(ownerFriend);
            friendRepository.save(memberFriend);
        } else if (optionalOwnerFriend.isPresent()) {
            log.error("Нарушена целостность данных. Пользователь {} имеет друга {}, но не наоборот",
                    ownerId, memberId);
            Friend ownerFriend = makeFriendDeleted(optionalOwnerFriend.get());
            friendRepository.save(ownerFriend);
        } else if (optionalMemberFriend.isPresent()) {
            log.error("Нарушена целостность данных. Пользователь {} имеет друга {}, но не наоборот",
                    memberId, ownerId);
            Friend friend2 = makeFriendDeleted(optionalMemberFriend.get());
            friendRepository.save(friend2);
        } else {
            throw new BadRequestException("Вы не являетесь друзьями с этим пользователем");
        }
    }

    /**
     * Метод для назначения параметров для сущности друга. <strong>Изменяет входящий объект и возвращает на
     * него ссылку.</strong>
     *
     * @param friend сущность друга.
     * @return ссылка на измененный входящий объект.
     */
    private Friend makeFriendDeleted(Friend friend) {
        friend.setIsDeleted(true);
        friend.setDeletedDate(LocalDateTime.now());

        return friend;
    }

}
