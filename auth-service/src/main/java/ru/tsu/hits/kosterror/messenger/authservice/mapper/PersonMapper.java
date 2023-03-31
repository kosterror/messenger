package ru.tsu.hits.kosterror.messenger.authservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.PersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.dto.person.RegisterPersonDto;
import ru.tsu.hits.kosterror.messenger.authservice.entity.Person;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface PersonMapper {

    Person registerDtoToEntity(RegisterPersonDto dto);

    PersonDto entityToDto(Person entity);

}