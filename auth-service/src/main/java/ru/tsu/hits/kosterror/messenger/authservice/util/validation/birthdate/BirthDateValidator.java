package ru.tsu.hits.kosterror.messenger.authservice.util.validation.birthdate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class BirthDateValidator implements ConstraintValidator<BirthDateValidation, LocalDate> {

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
        return birthDate != null
                && !birthDate.isAfter(LocalDate.now())
                && !birthDate.isBefore(LocalDate.of(1900, 1, 1));
    }

}
