package ru.tsu.hits.kosterror.messenger.chatservice.service.person;

import ru.tsu.hits.kosterror.messenger.chatservice.entity.RelationPerson;
import ru.tsu.hits.kosterror.messenger.core.dto.PersonDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Интерфейс для взаимодействия с сущностью {@link RelationPerson}.
 */
public interface RelationPersonService {

    /**
     * Создает и сохраняет в БД объект {@link RelationPerson}.
     *
     * @param personId идентификатор пользователя, для которого создается сущность.
     * @return сохраненная сущность.
     */
    RelationPerson createRelationPersonEntity(UUID personId);

    /**
     * Ищет {@link RelationPerson} по-заданному id.
     *
     * @param personId идентификатор пользователя.
     * @return сущность {@link RelationPerson}.
     */
    RelationPerson findRelationPersonEntity(UUID personId);

    /**
     * Создает список {@link RelationPerson}.
     *
     * @param personIds список идентификаторов.
     * @return список созданных и сохраненных сущностей.
     */
    List<RelationPerson> createAllRelationPerson(List<UUID> personIds);

    /**
     * Ищет объект {@link RelationPerson} обернутый в {@link Optional}.
     *
     * @param personId идентификатор пользователя.
     * @return объект {@link Optional} с найденной информацией о пользователе.
     */
    Optional<RelationPerson> findOptionalRelationPerson(UUID personId);

    /**
     * Обновляет сущность {@link RelationPerson} на основе {@link PersonDto}.
     *
     * @param personDto информация на основе которой нужно обновить сущности.
     */
    void synchronizeRelationPerson(PersonDto personDto);

}
