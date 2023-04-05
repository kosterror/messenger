package ru.tsu.hits.kosterror.messenger.authservice.util.validation.email;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tsu.hits.kosterror.messenger.authservice.repository.PersonRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Класс с бизнес-логикой для аннотации {@link UniqueEmail}
 */
@Component
@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final PersonRepository personRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return email != null && !personRepository.existsByEmail(email);
    }

}
