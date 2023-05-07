package ru.tsu.hits.kosterror.messenger.notificationservice.service.notifiocationinfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.messenger.core.dto.NumberDto;
import ru.tsu.hits.kosterror.messenger.notificationservice.repository.NotificationRepository;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationInfoServiceImpl implements NotificationInfoService {

    private final NotificationRepository repository;

    @Override
    public NumberDto countUncheckedNotifications(UUID personId) {
        Integer countUncheckedNotifications = repository.countByPersonIdAndIsChecked(personId, false);
        return new NumberDto(countUncheckedNotifications);
    }

}
