package ru.tsu.hits.kosterror.messenger.filestorageservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.kosterror.messenger.filestorageservice.entity.FileMetaData;

import java.util.UUID;

/**
 * JPA репозиторий для работы с БД для сущности {@link FileMetaData}.
 */
public interface FileMetaDataRepository extends JpaRepository<FileMetaData, UUID> {
}
