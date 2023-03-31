package ru.tsu.hits.kosterror.messenger.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tsu.hits.kosterror.messenger.authservice.entity.Person;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<Person, UUID> {

    Optional<Person> findByLogin(String login);

    boolean existsByLogin(String login);

    boolean existsByEmail(String email);

}
