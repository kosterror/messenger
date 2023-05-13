package ru.tsu.hits.kosterror.messenger.authservice.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.PersonCredentialsDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.RegisterPersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.token.FullPersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.entity.Person;
import ru.tsu.hits.kosterror.messenger.authservice.mapper.PersonMapper;
import ru.tsu.hits.kosterror.messenger.authservice.repository.PersonRepository;
import ru.tsu.hits.kosterror.messenger.core.dto.PersonDto;
import ru.tsu.hits.kosterror.messenger.core.exception.UnauthorizedException;
import ru.tsu.hits.kosterror.messenger.coresecurity.service.jwt.encoder.JwtEncoderService;

import java.time.LocalDate;

/**
 * Реализация интерфейса {@link AuthService}.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    public static final String INCORRECT_CREDENTIALS = "Неверный логин и/или пароль";
    private final PersonRepository repository;
    private final PersonMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoderService jwtEncoder;

    @Override
    public FullPersonDto register(RegisterPersonDto dto) {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        Person person = mapper.registerDtoToEntity(dto);
        person.setRegisterDate(LocalDate.now());
        person = repository.save(person);
        String token = jwtEncoder.generateToken(
                person.getLogin(),
                person.getId(),
                person.getEmail(),
                person.getFullName()
        );
        PersonDto personDto = mapper.entityToDto(person);

        return new FullPersonDto(token, personDto);
    }

    @Override
    public FullPersonDto login(PersonCredentialsDto dto) throws UnauthorizedException {
        Person person = repository
                .findByLogin(dto.getLogin())
                .orElseThrow(() -> new UnauthorizedException(INCORRECT_CREDENTIALS));

        if (!passwordEncoder.matches(dto.getPassword(), person.getPassword())) {
            throw new UnauthorizedException(INCORRECT_CREDENTIALS);
        }

        String token = jwtEncoder.generateToken(
                person.getLogin(),
                person.getId(),
                person.getEmail(),
                person.getFullName()
        );

        //TODO: отправить уведомление в notification-service об успешном логине

        PersonDto personDto = mapper.entityToDto(person);

        return new FullPersonDto(token, personDto);
    }

}
