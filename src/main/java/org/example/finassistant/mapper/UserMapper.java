package org.example.finassistant.mapper;

import org.example.finassistant.dto.UserDTO;
import org.example.finassistant.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserDTO userToDTO(User user);
}
