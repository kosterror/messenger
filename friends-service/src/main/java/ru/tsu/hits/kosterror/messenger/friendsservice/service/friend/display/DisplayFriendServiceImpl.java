package ru.tsu.hits.kosterror.messenger.friendsservice.service.friend.display;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.messenger.core.exception.BadRequestException;
import ru.tsu.hits.kosterror.messenger.core.request.PagingFilteringRequest;
import ru.tsu.hits.kosterror.messenger.core.response.PagingParamsResponse;
import ru.tsu.hits.kosterror.messenger.core.response.PagingResponse;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.FriendDto;
import ru.tsu.hits.kosterror.messenger.friendsservice.dto.request.FriendBasicFilters;
import ru.tsu.hits.kosterror.messenger.friendsservice.entity.Friend;
import ru.tsu.hits.kosterror.messenger.friendsservice.mapper.FriendMapper;
import ru.tsu.hits.kosterror.messenger.friendsservice.repository.FriendRepository;

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
    private final FriendRepository friendRepository;
    private final FriendMapper friendMapper;

    @Override
    public PagingResponse<List<FriendDto>> getFriends(UUID userId,
                                                      PagingFilteringRequest<FriendBasicFilters> request
    ) {
        if (request.getPaging().getPage() < 0 || request.getPaging().getSize() < 1) {
            log.error("Поступили некорректные параметры пагинации: page = '{}', size = '{}'",
                    request.getPaging().getPage(),
                    request.getPaging().getSize());
            throw new BadRequestException("Некорректные параметры пагинации");
        }

        Pageable pageable = PageRequest.of(request.getPaging().getPage(),
                request.getPaging().getSize(),
                Sort.Direction.ASC,
                FULL_NAME
        );

        Page<Friend> friendsPage;


        if (request.getFilters().getFriendFullName() == null || request.getFilters().getFriendFullName().isBlank()) {
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
