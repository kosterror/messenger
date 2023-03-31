package ru.tsu.hits.kosterror.messenger.authservice.util.validation.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tsu.hits.kosterror.messenger.authservice.repository.PersonRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


@Component
@RequiredArgsConstructor
public class UniqueLoginValidator implements ConstraintValidator<UniqueLogin, String> {

    private final PersonRepository repository;

    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        return login != null && !repository.existsByLogin(login);
    }
}
