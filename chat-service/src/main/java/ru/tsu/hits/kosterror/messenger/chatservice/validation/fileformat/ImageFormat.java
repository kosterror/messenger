package ru.tsu.hits.kosterror.messenger.chatservice.validation.fileformat;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

@Target({FIELD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageFormatValidator.class)
public @interface ImageFormat {

    String message() default "Файл не является изображением.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
