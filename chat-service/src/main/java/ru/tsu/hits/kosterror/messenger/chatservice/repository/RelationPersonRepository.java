package ru.tsu.hits.kosterror.messenger.chatservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.RelationPerson;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA репозиторий для сущности {@link RelationPerson}
 */
public interface RelationPersonRepository extends JpaRepository<RelationPerson, UUID> {

    Optional<RelationPerson> findByPersonId(UUID personId);

}
