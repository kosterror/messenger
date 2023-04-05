package ru.tsu.hits.kosterror.messenger.authservice.util.validation.datebetween;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * Аннотация для валидации даты между двумя другими датами.
 * <strong>Если поле {@code null}, то дата считается валидной</strong>.
 * Начальная и конечная даты должны передаваться в формате {@code yyyy-mm-dd}.
 * Если конечная дата не задана, то сравнение будет с текущей датой.
 */
@Target({FIELD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = DateBetweenValidator.class)
public @interface DateBetween {

    String message() default "Дата не находится между начальной и конечной датами";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String startDate();

    String finishDate() default "now";

}
