package ru.tsu.hits.kosterror.messenger.authservice.util.validation.birthdate;

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
@Constraint(validatedBy = BirthDateValidator.class)
public @interface BirthDateValidation {

    String message() default "Дата рождения должна быть не меньше 1900.01.01 и не больше текущей";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
