package ru.tsu.hits.kosterror.messenger.authservice.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.PersonCredentialsDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.PersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.RegisterPersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.token.FullPersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.entity.Person;
import ru.tsu.hits.kosterror.messenger.authservice.exception.UnauthorizedException;
import ru.tsu.hits.kosterror.messenger.authservice.mapper.PersonMapper;
import ru.tsu.hits.kosterror.messenger.authservice.repository.PersonRepository;
import ru.tsu.hits.kosterror.messenger.authservice.service.jwt.JwtService;

/**
 * Реализация интерфейса {@link AuthService}.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    public static final String INCORRECT_CREDENTIALS = "Неверный логин и/или пароль";
    private final PersonRepository repository;
    private final PersonMapper mapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public FullPersonDto register(RegisterPersonDto dto) {
        Person person = mapper.registerDtoToEntity(dto);
        person = repository.save(person);

        PersonDto personDto = mapper.entityToDto(person);
        String token = jwtService.generateAccessToken(person.getLogin());

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

        PersonDto personDto = mapper.entityToDto(person);
        String token = jwtService.generateAccessToken(person.getLogin());

        return new FullPersonDto(token, personDto);
    }

}
