package ru.tsu.hits.kosterror.messenger.friendsservice.service.blockedperson.display;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.messenger.core.dto.BooleanDto;
import ru.tsu.hits.kosterror.messenger.core.exception.BadRequestException;
import ru.tsu.hits.kosterror.messenger.core.exception.ForbiddenException;
import ru.tsu.hits.kosterror.messenger.core.exception.NotFoundException;
import ru.tsu.hits.kosterror.messenger.core.request.PagingFilteringRequest;
import ru.tsu.hits.kosterror.messenger.core.response.PagingParamsResponse;
import ru.tsu.hits.kosterror.messenger.core.response.PagingResponse;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.BlockedPersonDto;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.request.BlockedPersonBasicFilters;
import ru.tsu.hits.kosterror.messenger.friendsservice.entity.BlockedPerson;
import ru.tsu.hits.kosterror.messenger.friendsservice.mapper.BlockedPersonMapper;
import ru.tsu.hits.kosterror.messenger.friendsservice.repository.BlockedPersonRepository;
import ru.tsu.hits.kosterror.messenger.friendsservice.util.PageableBuilder;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Класс для получения черного списка пользователей, реализует интерфейс {@link DisplayBlockedPersonService}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DisplayBlockedPersonServiceImpl implements DisplayBlockedPersonService {

    private static final String FULL_NAME = "memberFullName";
    private final BlockedPersonRepository blockedPersonRepository;
    private final BlockedPersonMapper blockedPersonMapper;
    private final PageableBuilder pageableBuilder;

    @Override
    public BlockedPersonDto getBlockedPerson(UUID ownerId, UUID memberId) {
        if (ownerId.equals(memberId)) {
            throw new BadRequestException("Некорректный входные данные, " +
                    "идентификаторы пользователей совпадают");
        }

        if (blockedPersonRepository.existsByOwnerIdAndMemberIdAndIsDeleted(memberId, ownerId, false)) {
            throw new ForbiddenException("Пользователь добавил вас в чёрный список");
        }

        BlockedPerson blockedPerson = blockedPersonRepository
                .findBlockedPersonByOwnerIdAndMemberIdAndIsDeleted(ownerId, memberId, false)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден в черном списке"));

        return blockedPersonMapper.entityToDto(blockedPerson);
    }

    @Override

    public PagingResponse<List<BlockedPersonDto>> getBlockedPersons(
            UUID userId,
            PagingFilteringRequest<BlockedPersonBasicFilters> request
    ) {
        Pageable pageable = pageableBuilder.build(request.getPaging().getPage(),
                request.getPaging().getSize(),
                Sort.Direction.ASC,
                FULL_NAME
        );

        Page<BlockedPerson> blockedPersonPage = blockedPersonPage(userId,
                request,
                pageable
        );

        List<BlockedPersonDto> blockedPersonDtos = blockedPersonPage
                .getContent()
                .stream()
                .map(blockedPersonMapper::entityToDto)
                .collect(Collectors.toList());

        PagingParamsResponse pagingParams = new PagingParamsResponse(
                blockedPersonPage.getTotalPages(),
                blockedPersonPage.getTotalElements(),
                blockedPersonPage.getNumber(),
                blockedPersonDtos.size()
        );

        return new PagingResponse<>(pagingParams, blockedPersonDtos);
    }

    @Override
    public BooleanDto personIsBlocked(UUID ownerId, UUID memberId) {
        return new BooleanDto(
                blockedPersonRepository
                        .existsByOwnerIdAndMemberIdAndIsDeleted(ownerId, memberId, false)
        );
    }

    private Page<BlockedPerson> blockedPersonPage(UUID userId,
                                                  PagingFilteringRequest<BlockedPersonBasicFilters> request,
                                                  Pageable pageable) {
        Page<BlockedPerson> blockedPersonPage;

        if (request.getFilters().getBlockedPersonFullName() == null
                || request.getFilters().getBlockedPersonFullName().isBlank()) {
            blockedPersonPage = blockedPersonRepository
                    .getBlockedPersonsByOwnerIdAndIsDeleted(
                            userId,
                            false,
                            pageable
                    );
        } else {
            blockedPersonPage = blockedPersonRepository
                    .getBlockedPersonsByOwnerIdAndMemberFullNameContainingIgnoreCaseAndIsDeleted(
                            userId,
                            request.getFilters().getBlockedPersonFullName(),
                            false,
                            pageable
                    );
        }

        return blockedPersonPage;
    }

}
