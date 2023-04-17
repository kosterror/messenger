package ru.tsu.hits.kosterror.messenger.friendsservice.service.blockedperson.manage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.kosterror.messenger.core.exception.BadRequestException;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.BlockedPersonDto;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.CreateBlockedPersonDto;
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

    @Override
    @Transactional
    public BlockedPersonDto createBlockedPerson(UUID ownerId, CreateBlockedPersonDto memberDto) {
        if (ownerId.equals(memberDto.getId())) {
            throw new BadRequestException("Некорректный запрос, идентификаторы совпадают");
        }

        if (manageFriendService.isFriends(ownerId, memberDto.getId())) {
            manageFriendService.deleteFriend(ownerId, memberDto.getId());
        }

        BlockedPerson blockedPerson;
        Optional<BlockedPerson> blockedPersonOptional = blockedPersonRepository
                .findBlockedPersonByOwnerIdAndMemberId(ownerId, memberDto.getId());

        if (blockedPersonOptional.isPresent()) {
            blockedPerson = blockedPersonOptional.get();
            if (Boolean.TRUE.equals(blockedPerson.getIsDeleted())) {
                makeEntityBlocked(blockedPerson, memberDto);
            } else {
                throw new BadRequestException("Пользователь уже заблокирован");
            }
        } else {
            blockedPerson = new BlockedPerson();
            blockedPerson.setOwnerId(ownerId);
            makeEntityBlocked(blockedPerson, memberDto);
        }

        blockedPerson = blockedPersonRepository.save(blockedPerson);
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
    }

    /**
     * Метод, чтобы сделать сущность {@link BlockedPerson} активированной.
     *
     * @param entity сущность.
     * @param dto    информация о заблокированном пользователе.
     */
    private void makeEntityBlocked(BlockedPerson entity, CreateBlockedPersonDto dto) {
        entity.setMemberId(dto.getId());
        entity.setMemberFullName(dto.getFullName());
        entity.setAddedDate(LocalDate.now());
        entity.setIsDeleted(false);
        entity.setDeleteDate(null);
    }

}
