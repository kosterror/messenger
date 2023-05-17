package ru.tsu.hits.kosterror.messenger.chatservice.service.person;

import ru.tsu.hits.kosterror.messenger.chatservice.entity.RelationPerson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RelationPersonService {

    RelationPerson createRelationPersonEntity(UUID personId);

    RelationPerson findRelationPersonEntity(UUID personId);

    List<RelationPerson> createAllRelationPerson(List<UUID> personIds);

    Optional<RelationPerson> findOptionalRelationPerson(UUID personId);

}
