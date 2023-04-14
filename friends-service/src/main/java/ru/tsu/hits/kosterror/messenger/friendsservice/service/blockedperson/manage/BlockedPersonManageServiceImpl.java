package ru.tsu.hits.kosterror.messenger.friendsservice.service.blockedperson.manage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.messenger.core.exception.BadRequestException;
import ru.tsu.hits.kosterror.messenger.core.exception.NotFoundException;
import ru.tsu.hits.kosterror.messenger.friendsservice.entity.BlockedPerson;
import ru.tsu.hits.kosterror.messenger.friendsservice.repository.BlockedPersonRepository;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Класс, реализующий интерфейс {@link BlockedPersonManageService}
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BlockedPersonManageServiceImpl implements BlockedPersonManageService {

    private final BlockedPersonRepository blockedPersonRepository;

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

}
