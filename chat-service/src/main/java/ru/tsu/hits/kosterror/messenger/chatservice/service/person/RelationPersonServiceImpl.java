package ru.tsu.hits.kosterror.messenger.chatservice.service.person;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.RelationPerson;
import ru.tsu.hits.kosterror.messenger.chatservice.repository.RelationPersonRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RelationPersonServiceImpl implements RelationPersonService {

    private final RelationPersonRepository repository;

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
            result.add(new RelationPerson(personId, new ArrayList<>(), new ArrayList<>()));
        }

        return result;
    }

}
