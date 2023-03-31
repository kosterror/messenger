package ru.tsu.hits.kosterror.messenger.authservice.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.messenger.authservice.dto.Role;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.PersonCredentialsDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.RegisterPersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.token.PairTokenDto;
import ru.tsu.hits.kosterror.messenger.authservice.entity.Person;
import ru.tsu.hits.kosterror.messenger.authservice.exception.UnauthorizedException;
import ru.tsu.hits.kosterror.messenger.authservice.mapper.PersonMapper;
import ru.tsu.hits.kosterror.messenger.authservice.repository.PersonRepository;
import ru.tsu.hits.kosterror.messenger.authservice.service.jwt.JwtService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PersonRepository repository;
    private final PersonMapper mapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PairTokenDto register(RegisterPersonDto dto) {
        Person person = mapper.registerDtoToEntity(dto);
        person.setRole(Role.USER);
        person.setPassword(passwordEncoder.encode(dto.getPassword()));
        person = repository.save(person);

        String accessToken = jwtService.generateAccessToken(person.getLogin());

        return new PairTokenDto(accessToken, null);
    }

    @Override
    public PairTokenDto login(PersonCredentialsDto dto) throws UnauthorizedException {
        Person person = repository
                .findByLogin(dto.getLogin())
                .orElseThrow(() -> new UnauthorizedException("Неверный логин и/или пароль"));

        if (!passwordEncoder.matches(dto.getPassword(), person.getPassword())) {
            throw new UnauthorizedException("Неверный логин и/или пароль");
        }

        return new PairTokenDto(jwtService.generateAccessToken(person.getLogin()), null);

    }

}
