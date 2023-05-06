package ru.tsu.hits.kosterror.messenger.chatservice.validation.fileformat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.tsu.hits.kosterror.messenger.core.config.imageextensions.FileExtensions;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
@RequiredArgsConstructor
@Component
public class ImageFormatValidator implements ConstraintValidator<ImageFormat, MultipartFile> {

    private final FileExtensions fileExtensions;

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null) {
            return true;
        }

        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        return extension != null && fileExtensions.getImageExtensions().contains(extension);
    }

}

