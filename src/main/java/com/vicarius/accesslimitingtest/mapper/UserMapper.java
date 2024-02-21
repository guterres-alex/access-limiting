package com.vicarius.accesslimitingtest.mapper;

import com.vicarius.accesslimitingtest.dto.request.UserRequestDto;
import com.vicarius.accesslimitingtest.dto.response.UserResponseDto;
import com.vicarius.accesslimitingtest.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User userDtoToUser(UserRequestDto userRequestDto);
    UserResponseDto userToUserResponseDto(User user);

}