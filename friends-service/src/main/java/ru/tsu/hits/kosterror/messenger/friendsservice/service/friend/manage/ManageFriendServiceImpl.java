package ru.tsu.hits.kosterror.messenger.friendsservice.service.friend.manage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.kosterror.messenger.core.exception.BadRequestException;
import ru.tsu.hits.kosterror.messenger.core.exception.ConflictException;
import ru.tsu.hits.kosterror.messenger.core.exception.ForbiddenException;
import ru.tsu.hits.kosterror.messenger.coresecurity.model.JwtPersonData;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.CreateFriendDto;
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

    private final FriendRepository friendRepository;
    private final FriendMapper friendMapper;
    private final DisplayBlockedPersonService displayBlockedPersonService;

    @Override
    @Transactional
    public FriendDto createFriend(JwtPersonData jwtOwnerData, CreateFriendDto member) {
        CreateFriendDto owner = new CreateFriendDto(
                jwtOwnerData.getId(),
                jwtOwnerData.getFullName()
        );

        if (jwtOwnerData.getId() == member.getMemberId()) {
            throw new BadRequestException("Идентификаторы совпадают, нельзя добавить себя в друзья");
        }

        if (displayBlockedPersonService.personIsBlocked(member.getMemberId(), owner.getMemberId()).isValue()) {
            throw new BadRequestException("Нельзя добавить пользователя, который находится в черном списке");
        }

        if (displayBlockedPersonService.personIsBlocked(owner.getMemberId(), member.getMemberId()).isValue()) {
            throw new ForbiddenException("Пользователь добавил вас в черный список");
        }

        Optional<Friend> ownerFriendOptional = friendRepository
                .findFriendByOwnerIdAndMemberId(jwtOwnerData.getId(), member.getMemberId());
        Optional<Friend> memberFriendOptional = friendRepository
                .findFriendByOwnerIdAndMemberId(member.getMemberId(), jwtOwnerData.getId());

        Friend ownerFriend = makeFriend(ownerFriendOptional, owner, member);
        Friend memberFriend = makeFriend(memberFriendOptional, member, owner);

        ownerFriend = friendRepository.save(ownerFriend);
        friendRepository.save(memberFriend);

        return friendMapper.entityToDto(ownerFriend);
    }

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
            throw new ConflictException("Вы не являетесь друзьями с этим пользователем");
        }
    }

    @Override
    public boolean isFriends(UUID firstId, UUID secondId) {
        return friendRepository.existsByOwnerIdAndMemberIdAndIsDeleted(firstId, secondId, false)
                || friendRepository.existsByOwnerIdAndMemberIdAndIsDeleted(secondId, firstId, false);
    }

    /**
     * Метод для назначения параметров или создания сущности друга во время добавления его в друзья.
     *
     * @param friendOptional информация о сохраненной сущности друга.
     * @param owner          информация о целевом пользователе.
     * @param member         информация о внешнем пользователе.
     * @return актуальную сущность {@link Friend}.
     */
    private Friend makeFriend(Optional<Friend> friendOptional,
                              CreateFriendDto owner,
                              CreateFriendDto member
    ) {
        Friend ownerFriend;

        if (friendOptional.isPresent()) {
            ownerFriend = friendOptional.get();
            ownerFriend.setIsDeleted(false);
            ownerFriend.setDeletedDate(null);
            ownerFriend.setAddedDate(LocalDate.now());
            ownerFriend.setMemberFullName(member.getMemberFullName());
        } else {
            ownerFriend = new Friend(
                    owner.getMemberId(),
                    member.getMemberId(),
                    member.getMemberFullName(),
                    LocalDate.now(),
                    null,
                    false
            );
        }

        return friendRepository.save(ownerFriend);
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
        friend.setDeletedDate(LocalDate.now());

        return friend;
    }

}
