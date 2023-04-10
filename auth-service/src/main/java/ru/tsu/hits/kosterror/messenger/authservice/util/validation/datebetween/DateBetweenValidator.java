package ru.tsu.hits.kosterror.messenger.authservice.util.validation.datebetween;

import ru.tsu.hits.kosterror.messenger.core.exception.InternalException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Класс с бизнес-логикой для аннотации {@link DateBetween}.
 */
public class DateBetweenValidator implements ConstraintValidator<DateBetween, LocalDate> {

    private static final String ERROR_TEXT = "Задан некорректный формат для одной или всех" +
            " значений дат:  {'%s', '%s'} для валидации";
    private LocalDate start;
    private LocalDate finish;

    @Override
    public void initialize(DateBetween constraintAnnotation) {
        try {
            this.start = LocalDate.parse(constraintAnnotation.startDate());
            this.finish = constraintAnnotation.finishDate().equals("now") ?
                    LocalDate.now() : LocalDate.parse(constraintAnnotation.finishDate());

            ConstraintValidator.super.initialize(constraintAnnotation);
        } catch (DateTimeParseException exception) {
            throw new InternalException(String.format(ERROR_TEXT,
                    constraintAnnotation.startDate(),
                    constraintAnnotation.finishDate())
            );
        }

    }

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
        if (birthDate == null) {
            return true;
        }

        return birthDate.isAfter(start) && birthDate.isBefore(finish);
    }

}
