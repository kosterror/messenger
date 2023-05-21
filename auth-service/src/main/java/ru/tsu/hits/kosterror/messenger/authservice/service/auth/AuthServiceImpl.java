package ru.tsu.hits.kosterror.messenger.authservice.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.PersonCredentialsDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.RegisterPersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.token.FullPersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.entity.Person;
import ru.tsu.hits.kosterror.messenger.authservice.mapper.PersonMapper;
import ru.tsu.hits.kosterror.messenger.authservice.repository.PersonRepository;
import ru.tsu.hits.kosterror.messenger.core.dto.NewNotificationDto;
import ru.tsu.hits.kosterror.messenger.core.dto.PersonDto;
import ru.tsu.hits.kosterror.messenger.core.enumeration.NotificationType;
import ru.tsu.hits.kosterror.messenger.core.exception.UnauthorizedException;
import ru.tsu.hits.kosterror.messenger.coresecurity.service.jwt.encoder.JwtEncoderService;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.UUID;

import static ru.tsu.hits.kosterror.messenger.core.util.RabbitMQBindings.CREATE_NOTIFICATION_OUT;

/**
 * Реализация интерфейса {@link AuthService}.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    public static final String INCORRECT_CREDENTIALS = "Неверный логин и/или пароль";
    private static final String SUCCESS_LOGIN_MESSAGE = "Вы успешно аутентфиицировались";
    private final PersonRepository repository;
    private final PersonMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoderService jwtEncoder;
    private final StreamBridge streamBridge;

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
    @Transactional
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

        PersonDto personDto = mapper.entityToDto(person);
        sendSuccessLoginMessage(person.getId());
        return new FullPersonDto(token, personDto);
    }

    private void sendSuccessLoginMessage(UUID personId) {
        NewNotificationDto notification = NewNotificationDto
                .builder()
                .personId(personId)
                .type(NotificationType.LOGIN_SUCCESS)
                .message(SUCCESS_LOGIN_MESSAGE)
                .build();

        streamBridge.send(CREATE_NOTIFICATION_OUT, notification);
    }

}
