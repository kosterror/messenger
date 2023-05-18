package ru.tsu.hits.kosterror.messenger.chatservice.service.person;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.RelationPerson;
import ru.tsu.hits.kosterror.messenger.chatservice.repository.RelationPersonRepository;
import ru.tsu.hits.kosterror.messenger.core.dto.PersonDto;
import ru.tsu.hits.kosterror.messenger.core.exception.BadRequestException;
import ru.tsu.hits.kosterror.messenger.core.exception.ConflictException;
import ru.tsu.hits.kosterror.messenger.core.exception.InternalException;
import ru.tsu.hits.kosterror.messenger.core.exception.NotFoundException;
import ru.tsu.hits.kosterror.messenger.core.integration.auth.personinfo.IntegrationPersonInfoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RelationPersonServiceImpl implements RelationPersonService {

    private final RelationPersonRepository repository;
    private final IntegrationPersonInfoService integrationPersonInfoService;

    @Override
    public RelationPerson createRelationPersonEntity(UUID personId) {
        PersonDto personDto = getPersonDetails(personId);
        if (repository.findByPersonId(personId).isPresent()) {
            throw new ConflictException(String.format("RelationPerson с id = '%s' уже существует", personId));
        }

        RelationPerson relationPerson = RelationPerson
                .builder()
                .personId(personId)
                .fullName(personDto.getFullName())
                .avatarId(personDto.getAvatarId())
                .messages(new ArrayList<>())
                .chats(new ArrayList<>())
                .build();

        return repository.save(relationPerson);
    }

    @Override
    public Optional<RelationPerson> findOptionalRelationPerson(UUID personId) {
        return repository.findByPersonId(personId);
    }

    @Override
    public RelationPerson findRelationPersonEntity(UUID personId) {
        return repository
                .findByPersonId(personId)
                .orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден"));
    }

    @Override
    @Transactional
    public List<RelationPerson> createAllRelationPerson(List<UUID> personIds) {
        List<RelationPerson> resultList = new ArrayList<>();
        List<UUID> nonexistentPersonIds = new ArrayList<>();

        for (UUID personId : personIds) {
            Optional<RelationPerson> relationPersonOptional = repository.findByPersonId(personId);
            if (relationPersonOptional.isPresent()) {
                resultList.add(relationPersonOptional.get());
            } else {
                nonexistentPersonIds.add(personId);
            }
        }

        List<RelationPerson> toSave = buildRelationPersons(nonexistentPersonIds);
        toSave = repository.saveAll(toSave);
        resultList.addAll(toSave);

        return resultList;
    }

    private List<RelationPerson> buildRelationPersons(List<UUID> personIds) {
        List<RelationPerson> result = new ArrayList<>();

        for (UUID personId : personIds) {
            PersonDto personDetails = getPersonDetails(personId);
            result.add(RelationPerson
                    .builder()
                    .personId(personId)
                    .fullName(personDetails.getFullName())
                    .avatarId(personDetails.getAvatarId())
                    .build());
        }

        return result;
    }

    private PersonDto getPersonDetails(UUID personId) {
        try {
            return integrationPersonInfoService.getPersonInfo(personId);
        } catch (HttpClientErrorException.NotFound exception) {
            throw new BadRequestException("Не удалось найти пользователя с id = " + personId, exception);
        } catch (Exception e) {
            throw new InternalException("Ошибка во время создания RelationPerson", e);
        }
    }

}
