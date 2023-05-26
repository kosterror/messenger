package ru.tsu.hits.kosterror.messenger.filestorageservice.service.filename;

/**
 * Интерфейс предоставляющий методы для работы с именем файла.
 */
public interface FilenameService {

    /**
     * Конвертирует название файла в более универсальное, заменяя все пробелы на `_`,
     * а все символы на кириллице переводит в латиницу.
     *
     * @param rawFileName сырое имя файла.
     * @return преобразованное имя файла.
     */
    String convertToFilename(String rawFileName);

}
