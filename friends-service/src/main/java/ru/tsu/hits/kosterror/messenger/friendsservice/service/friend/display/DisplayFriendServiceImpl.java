package ru.tsu.hits.kosterror.messenger.friendsservice.service.friend.display;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.messenger.core.exception.BadRequestException;
import ru.tsu.hits.kosterror.messenger.core.exception.ForbiddenException;
import ru.tsu.hits.kosterror.messenger.core.exception.NotFoundException;
import ru.tsu.hits.kosterror.messenger.core.request.PagingFilteringRequest;
import ru.tsu.hits.kosterror.messenger.core.request.PagingParamsRequest;
import ru.tsu.hits.kosterror.messenger.core.response.PagingParamsResponse;
import ru.tsu.hits.kosterror.messenger.core.response.PagingResponse;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.FriendDto;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.request.FriendBasicFilters;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.request.FriendFilters;
import ru.tsu.hits.kosterror.messenger.friendsservice.entity.Friend;
import ru.tsu.hits.kosterror.messenger.friendsservice.mapper.FriendMapper;
import ru.tsu.hits.kosterror.messenger.friendsservice.repository.FriendRepository;
import ru.tsu.hits.kosterror.messenger.friendsservice.service.blockedperson.display.DisplayBlockedPersonService;
import ru.tsu.hits.kosterror.messenger.friendsservice.util.PageableBuilder;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Класс для получения друзей пользователей, реализует интерфейс {@link DisplayFriendService}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DisplayFriendServiceImpl implements DisplayFriendService {

    private static final String FULL_NAME = "memberFullName";
    private final DisplayBlockedPersonService displayBlockedPersonService;
    private final FriendRepository friendRepository;
    private final FriendMapper friendMapper;
    private final PageableBuilder pageableBuilder;

    @Override
    public FriendDto getFriend(UUID ownerId,
                               UUID memberId) {
        if (ownerId.equals(memberId)) {
            throw new BadRequestException("Некорректные входные данные, идентификаторы пользователей совпадают");
        }

        if (displayBlockedPersonService.personIsBlocked(memberId, ownerId).isValue()) {
            throw new ForbiddenException("Пользователь вас добавил в черный список.");
        }

        Friend friend = friendRepository
                .findFriendByOwnerIdAndMemberIdAndIsDeleted(ownerId, memberId, false)
                .orElseThrow(() -> new NotFoundException("Друг с заданным идентификатором не найден."));

        return friendMapper.entityToDto(friend);
    }

    @Override
    public PagingResponse<List<FriendDto>> getFriends(UUID userId,
                                                      PagingFilteringRequest<FriendBasicFilters> request
    ) {
        Pageable pageable = buildPageable(request.getPaging());

        Page<Friend> friendsPage = getFriendPage(userId,
                request,
                pageable
        );

        return buildPagingResponse(friendsPage);
    }

    @Override
    public PagingResponse<List<FriendDto>> searchFriends(UUID userId,
                                                         PagingFilteringRequest<FriendFilters> request
    ) {
        FriendFilters filters = request.getFilters();

        if (filters.getMemberFullName() != null) {
            filters.setMemberFullName(filters.getMemberFullName().toLowerCase());
        }

        Friend exampleFriend = new Friend();
        exampleFriend.setOwnerId(userId);
        exampleFriend.setMemberFullName(request.getFilters().getMemberFullName());
        exampleFriend.setAddedDate(filters.getAddedDate());
        exampleFriend.setIsDeleted(false);

        ExampleMatcher exampleMatcher = ExampleMatcher
                .matchingAll()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Friend> exampleEntity = Example.of(exampleFriend, exampleMatcher);
        Pageable pageable = buildPageable(request.getPaging());

        Page<Friend> friendPage = friendRepository.findAll(exampleEntity, pageable);

        return buildPagingResponse(friendPage);
    }

    /**
     * Метод для получения страницы друзей пользователя с учетом фильтрации и пагинации.
     *
     * @param userId   идентификатор пользователя, чьих друзей необходимо получить.
     * @param request  запрос на фильтрацию друзей.
     * @param pageable объект, содержащий информацию о запрошенной странице (номер страницы, размер страницы и т.д.).
     * @return - страница друзей пользователя с учетом заданных параметров фильтрации и пагинации.
     */
    private Page<Friend> getFriendPage(UUID userId,
                                       PagingFilteringRequest<FriendBasicFilters> request,
                                       Pageable pageable) {
        Page<Friend> friendsPage;

        if (request.getFilters().getFriendFullName() == null
                || request.getFilters().getFriendFullName().isBlank()) {
            friendsPage = friendRepository
                    .getFriendsByOwnerIdAndIsDeleted(
                            userId,
                            false,
                            pageable
                    );
        } else {
            friendsPage = friendRepository
                    .getFriendsByOwnerIdAndMemberFullNameContainingIgnoreCaseAndIsDeleted(
                            userId,
                            request.getFilters().getFriendFullName(),
                            false,
                            pageable
                    );
        }

        return friendsPage;
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
     * @param friendsPage объект Page, содержащий информацию о запрошенной странице,
     *                    полученный в результате запроса на фильтрацию друзей.
     * @return объект PagingResponse, содержащий информацию о запрошенной странице, с заданными параметрами.
     */
    private PagingResponse<List<FriendDto>> buildPagingResponse(Page<Friend> friendsPage) {
        List<FriendDto> friendDtos = friendsPage
                .getContent()
                .stream()
                .map(friendMapper::entityToDto)
                .collect(Collectors.toList());

        PagingParamsResponse pagingParams = new PagingParamsResponse(
                friendsPage.getTotalPages(),
                friendsPage.getTotalElements(),
                friendsPage.getNumber(),
                friendDtos.size()
        );

        return new PagingResponse<>(pagingParams, friendDtos);
    }

}
