package ru.tsu.hits.kosterror.messenger.friendsservice.service.blockedperson.display;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.messenger.core.dto.BooleanDto;
import ru.tsu.hits.kosterror.messenger.core.exception.BadRequestException;
import ru.tsu.hits.kosterror.messenger.core.exception.ForbiddenException;
import ru.tsu.hits.kosterror.messenger.core.exception.NotFoundException;
import ru.tsu.hits.kosterror.messenger.core.request.PagingFilteringRequest;
import ru.tsu.hits.kosterror.messenger.core.request.PagingParamsRequest;
import ru.tsu.hits.kosterror.messenger.core.response.PagingParamsResponse;
import ru.tsu.hits.kosterror.messenger.core.response.PagingResponse;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.BlockedPersonDto;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.request.BlockedPersonBasicFilters;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.request.BlockedPersonFilters;
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
        Pageable pageable = buildPageable(request.getPaging());

        Page<BlockedPerson> blockedPersonPage = blockedPersonPage(userId,
                request,
                pageable
        );

        return buildPagingResponse(blockedPersonPage);
    }

    @Override
    public BooleanDto personIsBlocked(UUID ownerId, UUID memberId) {
        return new BooleanDto(
                blockedPersonRepository
                        .existsByOwnerIdAndMemberIdAndIsDeleted(ownerId, memberId, false)
        );
    }

    @Override
    public PagingResponse<List<BlockedPersonDto>> searchBlockedPersons(UUID userId,
                                                                       PagingFilteringRequest<BlockedPersonFilters>
                                                                               request
    ) {
        BlockedPersonFilters filters = request.getFilters();
        if (filters.getMemberFullName() != null) {
            filters.setMemberFullName(filters.getMemberFullName().toLowerCase());
        }

        BlockedPerson exampleBlockedPerson = new BlockedPerson();
        exampleBlockedPerson.setOwnerId(userId);
        exampleBlockedPerson.setMemberFullName(filters.getMemberFullName());
        exampleBlockedPerson.setAddedDate(filters.getAddedDate());
        exampleBlockedPerson.setIsDeleted(false);

        ExampleMatcher exampleMatcher = ExampleMatcher
                .matchingAll()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<BlockedPerson> exampleEntity = Example.of(exampleBlockedPerson, exampleMatcher);
        Pageable pageable = buildPageable(request.getPaging());
        Page<BlockedPerson> blockedPersonPage = blockedPersonRepository.findAll(exampleEntity, pageable);
        return buildPagingResponse(blockedPersonPage);
    }

    /**
     * Метод для получения страницы заблокированных пользователей с учетом фильтрации и пагинации.
     *
     * @param userId   идентификатор целевого пользователя.
     * @param request  запрос с фильтрацией и пагинацией.
     * @param pageable объект, содержащий информацию о запрошенной странице (номер страницы, размер страницы и т.д.).
     * @return - страница заблокированных пользователей с учетом заданных параметров фильтрации и пагинации.
     */
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

    /**
     * Метод для построения объекта {@link Pageable} на основе {@code request}.
     *
     * @param request объект, содержащий информацию о странице, размере и т.д.
     * @return объект {@link Pageable}, созданный с учетом входных параметров.
     */
    private Pageable buildPageable(PagingParamsRequest request) {
        return pageableBuilder.build(request.getPage(),
                request.getSize(),
                Sort.Direction.ASC,
                FULL_NAME
        );
    }

    /**
     * Метод для построения объекта PagingResponse, содержащего информацию о запрошенной странице.
     *
     * @param blockedPersonPage объект Page, содержащий информацию о запрошенной странице,
     *                          полученный в результате запроса на фильтрацию друзей.
     * @return объект PagingResponse, содержащий информацию о запрошенной странице, с заданными параметрами.
     */
    private PagingResponse<List<BlockedPersonDto>> buildPagingResponse(Page<BlockedPerson> blockedPersonPage) {
        List<BlockedPersonDto> friendDtos = blockedPersonPage
                .getContent()
                .stream()
                .map(blockedPersonMapper::entityToDto)
                .collect(Collectors.toList());

        PagingParamsResponse pagingParams = new PagingParamsResponse(
                blockedPersonPage.getTotalPages(),
                blockedPersonPage.getTotalElements(),
                blockedPersonPage.getNumber(),
                friendDtos.size()
        );

        return new PagingResponse<>(pagingParams, friendDtos);
    }

}
