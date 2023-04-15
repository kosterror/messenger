package ru.tsu.hits.kosterror.messenger.friendsservice.service.blockedperson.manage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.kosterror.messenger.core.exception.BadRequestException;
import ru.tsu.hits.kosterror.messenger.core.exception.ConflictException;
import ru.tsu.hits.kosterror.messenger.core.exception.NotFoundException;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.BlockedPersonDto;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.CreateBlockedPersonDto;
import ru.tsu.hits.kosterror.messenger.friendsservice.entity.BlockedPerson;
import ru.tsu.hits.kosterror.messenger.friendsservice.mapper.BlockedPersonMapper;
import ru.tsu.hits.kosterror.messenger.friendsservice.repository.BlockedPersonRepository;
import ru.tsu.hits.kosterror.messenger.friendsservice.service.friend.manage.ManageFriendService;

import java.time.LocalDateTime;
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

    @Override
    @Transactional
    public BlockedPersonDto createBlockedPerson(UUID ownerId, CreateBlockedPersonDto memberDto) {
        if (manageFriendService.isFriends(ownerId, memberDto.getMemberId())) {
            manageFriendService.deleteFriend(ownerId, memberDto.getMemberId());
            log.info("Пользователь {} дружит с пользователем {}", ownerId, memberDto.getMemberId());
        } else {
            log.info("Пользователи не дружат");
        }

        BlockedPerson blockedPerson = makeBlockedPerson(ownerId, memberDto);
        blockedPerson = blockedPersonRepository.save(blockedPerson);
        return blockedPersonMapper.entityToDto(blockedPerson);
    }

    @Override
    public void deleteBlockedPerson(UUID ownerId, UUID memberId) {
        if (ownerId == memberId) {
            throw new BadRequestException("Некорректный запрос. Идентификаторы совпадают.");
        }
        BlockedPerson blockedPerson = blockedPersonRepository
                .findBlockedPersonByOwnerIdAndMemberIdAndIsDeleted(ownerId, memberId, false)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id = '%s' не" +
                        " заблокировал пользователя с id = '%s'", ownerId, memberId)));

        blockedPerson.setIsDeleted(true);
        blockedPerson.setDeleteDate(LocalDateTime.now());
        blockedPersonRepository.save(blockedPerson);
    }

    /**
     * Создает объект {@link BlockedPerson} на основе переданных параметров. Если пользователь
     * с заданным идентификатором уже находится в чёрном списке, то обновляет его данные.
     * Если запись о нём была помечена как удаленная, то создает новую запись.
     *
     * @param ownerId   идентификатор пользователя, который добавляет в чёрный список.
     * @param memberDto DTO с информацией о пользователе, которого нужно заблокировать.
     * @return объект {@link BlockedPerson}.
     */
    private BlockedPerson makeBlockedPerson(UUID ownerId, CreateBlockedPersonDto memberDto) {
        Optional<BlockedPerson> blockedPersonOptional =
                blockedPersonRepository.findBlockedPersonByOwnerIdAndMemberId(ownerId, memberDto.getMemberId());

        BlockedPerson blockedPerson;

        if (blockedPersonOptional.isPresent()) {
            blockedPerson = blockedPersonOptional.get();
            if (Boolean.FALSE.equals(blockedPerson.getIsDeleted())) {
                throw new ConflictException(String.format("Пользователь с id '%s' уже в чёрном списке",
                        memberDto.getMemberId())
                );
            }
            blockedPerson.setMemberFullName(memberDto.getMemberFullName());
            blockedPerson.setAddedDate(LocalDateTime.now());
            blockedPerson.setDeleteDate(null);
            blockedPerson.setIsDeleted(false);
        } else {
            blockedPerson = new BlockedPerson(
                    ownerId,
                    memberDto.getMemberId(),
                    memberDto.getMemberFullName(),
                    LocalDateTime.now(),
                    null,
                    false
            );
        }

        return blockedPerson;
    }

}
