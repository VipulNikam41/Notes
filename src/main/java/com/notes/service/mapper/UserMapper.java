package com.notes.service.mapper;

import com.notes.dto.CreateUser;
import com.notes.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    User dtoToEntity(CreateUser createUser);
}
