package ru.tsu.hits.kosterror.messenger.filestorageservice.service.filestorage;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.tsu.hits.kosterror.messenger.core.dto.FileMetaDataDto;
import ru.tsu.hits.kosterror.messenger.core.exception.InternalException;
import ru.tsu.hits.kosterror.messenger.core.exception.NotFoundException;
import ru.tsu.hits.kosterror.messenger.filestorageservice.config.MinioConfig;
import ru.tsu.hits.kosterror.messenger.filestorageservice.entity.FileMetaData;
import ru.tsu.hits.kosterror.messenger.filestorageservice.mapper.FileMetaDataMapper;
import ru.tsu.hits.kosterror.messenger.filestorageservice.repository.FileMetaDataRepository;
import ru.tsu.hits.kosterror.messenger.filestorageservice.service.filename.FilenameService;

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
    private final FilenameService fileNameService;

    @Override
    public FileMetaDataDto uploadFile(@NonNull UUID authorId,
                                      @NonNull MultipartFile file) {
        try {
            FileMetaData metaData = buildFileMetaData(authorId, file);

            PutObjectArgs putObjectArgs = buildPutObjectArgs(metaData, file);
            minioClient.putObject(putObjectArgs);

            metaData = fileMetaDataRepository.save(metaData);
            return fileMetaDataMapper.entityToDto(metaData);
        } catch (Exception exception) {
            log.error("Ошибка во время загрузки файла в S3", exception);
            throw new InternalException("Ошибка во время загрузки файла в S3", exception);
        }
    }

    @Override
    public Pair<String, byte[]> downloadFileAndFilename(@NonNull UUID fileId) {
        String filename = fileMetaDataRepository
                .findById(fileId)
                .orElseThrow(() -> new NotFoundException(String.format("Файл с id = '%s' не найден", fileId)))
                .getName();

        GetObjectArgs getObjectArgs = GetObjectArgs
                .builder()
                .bucket(minioConfig.getBucket())
                .object(fileId.toString())
                .build();

        try (var in = minioClient.getObject(getObjectArgs)) {
            return Pair.of(filename, in.readAllBytes());
        } catch (Exception e) {
            log.error("Ошибка при скачивании файла из S3 с id = '{}'", fileId);
            throw new InternalException("Ошибка при скачивании файла из S3 c id = " + fileId, e);
        }

    }

    @Override
    public FileMetaDataDto getFileMetaData(@NonNull UUID fileId) {
        FileMetaData fileMetaData = fileMetaDataRepository
                .findById(fileId)
                .orElseThrow(() -> new NotFoundException(String.format("Файл с id = '%s' не найден", fileId)));

        return fileMetaDataMapper.entityToDto(fileMetaData);
    }

    private FileMetaData buildFileMetaData(UUID authorId, MultipartFile file) {
        String rawFilename = file.getOriginalFilename();
        String convertedFilename = fileNameService.convertToFilename(rawFilename);

        return FileMetaData
                .builder()
                .id(UUID.randomUUID())
                .name(convertedFilename)
                .uploadDateTime(LocalDateTime.now())
                .authorId(authorId)
                .sizeInBytes(file.getSize())
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
