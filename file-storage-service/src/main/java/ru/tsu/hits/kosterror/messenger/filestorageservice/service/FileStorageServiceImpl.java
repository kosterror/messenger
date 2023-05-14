package ru.tsu.hits.kosterror.messenger.filestorageservice.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.tsu.hits.kosterror.messenger.core.dto.FileMetaDataDto;
import ru.tsu.hits.kosterror.messenger.core.exception.InternalException;
import ru.tsu.hits.kosterror.messenger.filestorageservice.config.MinioConfig;
import ru.tsu.hits.kosterror.messenger.filestorageservice.entity.FileMetaData;
import ru.tsu.hits.kosterror.messenger.filestorageservice.mapper.FileMetaDataMapper;
import ru.tsu.hits.kosterror.messenger.filestorageservice.repository.FileMetaDataRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private final FileMetaDataRepository fileMetaDataRepository;
    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    private final FileMetaDataMapper fileMetaDataMapper;

    @Override
    public FileMetaDataDto uploadFile(@NonNull UUID authorId,
                                      @NonNull MultipartFile file) {
        try {
            FileMetaData metaData = buildFileMetaData(authorId, file.getOriginalFilename());

            PutObjectArgs putObjectArgs = buildPutObjectArgs(metaData, file);
            minioClient.putObject(putObjectArgs);

            metaData = fileMetaDataRepository.save(metaData);
            return fileMetaDataMapper.entityToDto(metaData);
        } catch (Exception exception) {
            log.error("Ошибка во время загрузки файла в S3", exception);
            throw new InternalException("Ошибка во время загрузки файла в S3", exception);
        }
    }

    private FileMetaData buildFileMetaData(UUID authorId, String filename) {
        return FileMetaData
                .builder()
                .id(UUID.randomUUID())
                .name(filename)
                .uploadDateTime(LocalDateTime.now())
                .authorId(authorId)
                .build();
    }

    private PutObjectArgs buildPutObjectArgs(FileMetaData metaData, MultipartFile file) throws IOException {
        byte[] content = file.getBytes();

        return PutObjectArgs
                .builder()
                .bucket(minioConfig.getBucket())
                .object(metaData.getId().toString())
                .stream(new ByteArrayInputStream(content), content.length, -1)
                .build();
    }

}
