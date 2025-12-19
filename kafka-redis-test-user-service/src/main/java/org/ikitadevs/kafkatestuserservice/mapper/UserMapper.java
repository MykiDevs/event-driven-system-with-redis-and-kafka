package org.ikitadevs.kafkatestuserservice.mapper;

import org.ikitadevs.kafkatestuserservice.dto.request.UserUpdateDto;
import org.ikitadevs.kafkatestuserservice.dto.response.UserDto;
import org.ikitadevs.kafkatestuserservice.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserDto userDto);
    UserDto toDto(User user);

    void updateUserFromDto(UserUpdateDto dto, @MappingTarget User user);
}
