package com.management_user.service;

import com.management_user.dtos.CredentialsDto;
import com.management_user.dtos.SignUpDto;
import com.management_user.dtos.UserDto;
import com.management_user.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface UserService {

  public UserDto login(CredentialsDto credentialsDto);

  public UserDto register(SignUpDto userDto);

  public void confirmEmail(String email);
  public UserDto findByLogin(String login);
}
