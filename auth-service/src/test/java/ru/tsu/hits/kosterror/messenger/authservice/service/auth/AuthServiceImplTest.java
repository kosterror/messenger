package ru.tsu.hits.kosterror.messenger.authservice.service.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.PersonCredentialsDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.RegisterPersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.token.FullPersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.entity.Person;
import ru.tsu.hits.kosterror.messenger.authservice.mapper.PersonMapper;
import ru.tsu.hits.kosterror.messenger.authservice.repository.PersonRepository;
import ru.tsu.hits.kosterror.messenger.core.dto.NewNotificationDto;
import ru.tsu.hits.kosterror.messenger.core.dto.PersonDto;
import ru.tsu.hits.kosterror.messenger.core.exception.UnauthorizedException;
import ru.tsu.hits.kosterror.messenger.coresecurity.service.jwt.encoder.JwtEncoderService;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.tsu.hits.kosterror.messenger.core.util.RabbitMQBindings.CREATE_NOTIFICATION_OUT;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private PersonRepository repository;
    @Mock
    private PersonMapper personMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtEncoderService jwtEncoderService;
    @Mock
    private StreamBridge streamBridge;
    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void register_ShouldRegisterPersonAndReturnFullPersonDto() throws NoSuchFieldException, IllegalAccessException {
        RegisterPersonDto registerPersonDto = new RegisterPersonDto();
        registerPersonDto.setLogin("testlogin");
        registerPersonDto.setPassword("testpassword");
        registerPersonDto.setEmail("testemail@example.com");
        registerPersonDto.setFullName("Test User");

        Person person = createPerson();

        PersonDto personDto = new PersonDto();
        personDto.setId(person.getId());
        personDto.setLogin(person.getLogin());
        personDto.setEmail(person.getEmail());
        personDto.setFullName(person.getFullName());

        String token = "generatedtoken";

        when(passwordEncoder.encode(registerPersonDto.getPassword())).thenReturn("encodedpassword");
        when(personMapper.registerDtoToEntity(registerPersonDto)).thenReturn(person);
        when(repository.save(person)).thenReturn(person);
        when(jwtEncoderService.generateToken(person.getLogin(), person.getId(), person.getEmail(), person.getFullName())).thenReturn(token);
        when(personMapper.entityToDto(person)).thenReturn(personDto);

        FullPersonDto result = authService.register(registerPersonDto);

        assertNotNull(result);
        assertEquals(token, result.getAccessToken());
        assertEquals(personDto, result.getPersonDto());
        verify(passwordEncoder).encode(registerPersonDto.getPassword());
        verify(personMapper).registerDtoToEntity(registerPersonDto);
        verify(repository).save(person);
        verify(jwtEncoderService).generateToken(person.getLogin(), person.getId(), person.getEmail(), person.getFullName());
        verify(personMapper).entityToDto(person);
    }

    @Test
    void login_WithCorrectCredentials_ShouldReturnFullPersonDto() throws UnauthorizedException, NoSuchFieldException, IllegalAccessException {
        PersonCredentialsDto personCredentialsDto = new PersonCredentialsDto();
        personCredentialsDto.setLogin("testlogin");
        personCredentialsDto.setPassword("testpassword");

        Person person = createPerson();

        PersonDto personDto = new PersonDto();
        personDto.setId(person.getId());
        personDto.setLogin(person.getLogin());
        personDto.setEmail(person.getEmail());
        personDto.setFullName(person.getFullName());

        String token = "generatedtoken";

        when(repository.findByLogin(personCredentialsDto.getLogin())).thenReturn(Optional.of(person));
        when(passwordEncoder.matches(personCredentialsDto.getPassword(), person.getPassword())).thenReturn(true);
        when(jwtEncoderService.generateToken(person.getLogin(), person.getId(), person.getEmail(), person.getFullName())).thenReturn(token);
        when(personMapper.entityToDto(person)).thenReturn(personDto);

        FullPersonDto result = authService.login(personCredentialsDto);

        assertNotNull(result);
        assertEquals(token, result.getAccessToken());
        assertEquals(personDto, result.getPersonDto());
        verify(repository).findByLogin(personCredentialsDto.getLogin());
        verify(passwordEncoder).matches(personCredentialsDto.getPassword(), person.getPassword());
        verify(jwtEncoderService).generateToken(person.getLogin(), person.getId(), person.getEmail(), person.getFullName());
        verify(personMapper).entityToDto(person);
        verify(streamBridge).send(eq(CREATE_NOTIFICATION_OUT),any(NewNotificationDto.class));
    }

    @Test
    void login_WithIncorrectCredentials_ShouldThrowUnauthorizedException() {
        PersonCredentialsDto personCredentialsDto = new PersonCredentialsDto();
        personCredentialsDto.setLogin("testlogin");
        personCredentialsDto.setPassword("testpassword");

        when(repository.findByLogin(personCredentialsDto.getLogin())).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class, () -> authService.login(personCredentialsDto));

        verify(repository).findByLogin(personCredentialsDto.getLogin());
        verifyNoMoreInteractions(passwordEncoder, jwtEncoderService, personMapper, streamBridge);
    }

    @Test
    void login_WithIncorrectPassword_ShouldThrowUnauthorizedException() throws NoSuchFieldException, IllegalAccessException {
        PersonCredentialsDto personCredentialsDto = new PersonCredentialsDto();
        personCredentialsDto.setLogin("testlogin");
        personCredentialsDto.setPassword("testpassword");

        Person person = createPerson();

        when(repository.findByLogin(personCredentialsDto.getLogin())).thenReturn(Optional.of(person));
        when(passwordEncoder.matches(personCredentialsDto.getPassword(), person.getPassword())).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> authService.login(personCredentialsDto));

        verify(repository).findByLogin(personCredentialsDto.getLogin());
        verify(passwordEncoder).matches(personCredentialsDto.getPassword(), person.getPassword());
        verifyNoMoreInteractions(jwtEncoderService, personMapper, streamBridge);
    }

    private Person createPerson() throws NoSuchFieldException, IllegalAccessException {
        Person person = new Person();
        person.setLogin("testlogin");
        person.setPassword("encodedpassword");
        person.setEmail("testemail@example.com");
        person.setFullName("Test User");
        person.setRegisterDate(LocalDate.now());

        // Set the id using reflection
        Field idField = Person.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(person, UUID.randomUUID());

        return person;
    }

}