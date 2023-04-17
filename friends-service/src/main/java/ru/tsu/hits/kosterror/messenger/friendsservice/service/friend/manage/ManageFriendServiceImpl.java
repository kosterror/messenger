package ru.tsu.hits.kosterror.messenger.friendsservice.service.friend.manage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.kosterror.messenger.core.exception.BadRequestException;
import ru.tsu.hits.kosterror.messenger.core.exception.ConflictException;
import ru.tsu.hits.kosterror.messenger.core.exception.ForbiddenException;
import ru.tsu.hits.kosterror.messenger.core.exception.InternalException;
import ru.tsu.hits.kosterror.messenger.coresecurity.model.JwtPersonData;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.CreateFriendDto;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.FriendDto;
import ru.tsu.hits.kosterror.messenger.friendsservice.entity.Friend;
import ru.tsu.hits.kosterror.messenger.friendsservice.mapper.FriendMapper;
import ru.tsu.hits.kosterror.messenger.friendsservice.repository.FriendRepository;
import ru.tsu.hits.kosterror.messenger.friendsservice.service.blockedperson.display.DisplayBlockedPersonService;

import java.time.LocalDate;
import java.util.Objects;
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

        if (jwtOwnerData.getId().equals(member.getId())) {
            throw new BadRequestException("Идентификаторы совпадают, нельзя добавить себя в друзья");
        }
        if (displayBlockedPersonService.personIsBlocked(owner.getId(), member.getId()).isValue()) {
            throw new BadRequestException("Нельзя добавить пользователя, который находится в черном списке");
        }
        if (displayBlockedPersonService.personIsBlocked(member.getId(), owner.getId()).isValue()) {
            throw new ForbiddenException("Пользователь добавил вас в черный список");
        }

        Optional<Friend> ownerFriendOptional = friendRepository
                .findFriendByOwnerIdAndMemberId(owner.getId(), member.getId());
        Optional<Friend> memberFriendOptional = friendRepository
                .findFriendByOwnerIdAndMemberId(member.getId(), owner.getId());

        Friend ownerFriend;
        Friend memberFriend;

        if (ownerFriendOptional.isPresent() && memberFriendOptional.isPresent()) {
            ownerFriend = ownerFriendOptional.get();
            memberFriend = memberFriendOptional.get();

            ownerFriend.setOwnerId(owner.getId());
            ownerFriend.setMemberId(member.getId());

            if (Objects.equals(ownerFriend.getIsDeleted(), memberFriend.getIsDeleted())) {
                if (Boolean.TRUE.equals(ownerFriend.getIsDeleted())) {
                    ownerFriend.setIsDeleted(false);
                    memberFriend.setIsDeleted(false);
                    ownerFriend.setAddedDate(LocalDate.now());
                    memberFriend.setAddedDate(LocalDate.now());
                    ownerFriend.setDeletedDate(null);
                    memberFriend.setDeletedDate(null);
                } else {
                    throw new BadRequestException("Вы уже являетесь друзьями");
                }
            } else {
                throw new InternalException(String.format("Нарушена целостность данных. Связь дружбы является" +
                                " односторонней для пользователей: '%s', '%s'", ownerFriend.getOwnerId(),
                        memberFriend.getOwnerId()));
            }
        } else if (ownerFriendOptional.isEmpty() && memberFriendOptional.isEmpty()) {
            ownerFriend = buildFriend(owner.getId(), member);
            memberFriend = buildFriend(member.getId(), owner);
        } else {
            throw new InternalException(String.format("Нарушена целостность данных. Связь дружбы является" +
                            " односторонней для пользователей: '%s', '%s'", owner.getId(),
                    member.getId()));
        }

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

    /**
     * Метод для создания {@link Friend} на основе входных параметров.
     *
     * @param ownerId идентификатор целевого пользователя.
     * @param dto     информация о внешнем пользователе.
     * @return новый объект {@link Friend}.
     */
    private Friend buildFriend(UUID ownerId, CreateFriendDto dto) {
        Friend friend = new Friend();
        friend.setOwnerId(ownerId);
        friend.setMemberId(dto.getId());
        friend.setMemberFullName(dto.getFullName());
        friend.setDeletedDate(null);
        friend.setIsDeleted(false);
        friend.setAddedDate(LocalDate.now());

        return friend;
    }

}
