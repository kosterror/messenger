package ru.tsu.hits.kosterror.messenger.authservice.util.validation.login;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * Аннотация для проверки на уникальность логина пользователя.
 */
@Target({FIELD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UniqueLoginValidator.class)
public @interface UniqueLogin {

    String message() default "Логин должен быть уникальным";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
