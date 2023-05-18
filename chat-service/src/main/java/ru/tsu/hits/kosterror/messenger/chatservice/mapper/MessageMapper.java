package ru.tsu.hits.kosterror.messenger.chatservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.tsu.hits.kosterror.messenger.chatservice.dto.MessageDto;
import ru.tsu.hits.kosterror.messenger.chatservice.entity.Message;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MessageMapper {

    @Mapping(target = "authorId", source = "relationPerson.personId")
    @Mapping(target = "authorFullName", source = "relationPerson.fullName")
    @Mapping(target = "authorAvatarId", source = "relationPerson.avatarId")
    @Mapping(target = "attachments", source = "attachments")
    MessageDto entityToDto(Message entity);

}
