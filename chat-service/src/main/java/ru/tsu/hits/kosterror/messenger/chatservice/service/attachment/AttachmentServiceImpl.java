package ru.tsu.hits.kosterror.messenger.chatservice.service.attachment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.Attachment;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.Message;
import ru.tsu.hits.kosterror.messenger.chatservice.repository.AttachmentRepository;
import ru.tsu.hits.kosterror.messenger.core.dto.FileMetaDataDto;
import ru.tsu.hits.kosterror.messenger.core.exception.ForbiddenException;
import ru.tsu.hits.kosterror.messenger.core.exception.InternalException;
import ru.tsu.hits.kosterror.messenger.core.exception.NotFoundException;
import ru.tsu.hits.kosterror.messenger.core.integration.filestorage.FileStorageIntegrationService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final FileStorageIntegrationService fileStorageIntegrationService;

    @Override
    public Attachment saveAttachment(Message message, UUID fileId, UUID authorId) {
        FileMetaDataDto fileMetaData = getFileMetaData(fileId);
        if (!authorId.equals(fileMetaData.getAuthorId())) {
            throw new ForbiddenException(String.format("Файл с id = '%s' чужой", fileId));
        }

        Attachment attachment = Attachment
                .builder()
                .name(fileMetaData.getName())
                .fileId(fileMetaData.getFileId())
                .sizeInBytes(fileMetaData.getSizeInBytes())
                .message(message)
                .build();

        return attachmentRepository.save(attachment);
    }

    private FileMetaDataDto getFileMetaData(UUID fileId) {
        try {
            return fileStorageIntegrationService.getFileMetaData(fileId);
        } catch (HttpClientErrorException.NotFound exception) {
            throw new NotFoundException(String.format("Файл с id = %s не найден", fileId), exception);
        } catch (RestClientException exception) {
            throw new InternalException("Ошибка во время интеграционного запроса " +
                    "для получения метаинформации о файле");
        }
    }

}
