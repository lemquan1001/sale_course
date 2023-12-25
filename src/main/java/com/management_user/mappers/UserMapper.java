package com.management_user.mappers;

import com.management_user.dtos.SignUpDto;
import com.management_user.dtos.UserDto;
import com.management_user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserDto toUserDto(User user);

  @Mapping(target="password",ignore = true)
  User signUpToUser(SignUpDto userDto);
}
