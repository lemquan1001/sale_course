package com.management_user.service.impl;

import com.management_user.dtos.CredentialsDto;
import com.management_user.dtos.SignUpDto;
import com.management_user.dtos.UserDto;
import com.management_user.email.EmailSender;
import com.management_user.entity.User;
import com.management_user.exceptions.AppException;
import com.management_user.mappers.UserMapper;
import com.management_user.repository.UserRepo;
import com.management_user.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.nio.CharBuffer;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class User_Impl implements UserService {

  private final UserRepo userRepository;

  private final PasswordEncoder passwordEncoder;

  private final UserMapper userMapper;

  private final EmailSender emailSender;

  public UserDto login(CredentialsDto credentialsDto) {
    // Kiểm tra login không chứa khoảng trắng
    checkNotSpace(credentialsDto.login());

    User user = userRepository.findByLogin(credentialsDto.login())
      .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

    if (user.getEnable() == 0) {
      throw new AppException("Email not verified", HttpStatus.BAD_REQUEST);
    }

    if (passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()), user.getPassword())) {
      return userMapper.toUserDto(user);
    }
    throw new AppException("Invalid password", HttpStatus.BAD_REQUEST);
  }

  public UserDto register(SignUpDto userDto) {

    // Kiểm tra và giới hạn độ dài của các trường
    if (isInvalidLength(userDto.userName(), 50) ||
            isInvalidLength(userDto.login(), 50)) {
      throw new AppException("Invalid input length", HttpStatus.BAD_REQUEST);
    }
    // Kiểm tra định dạng email
    if (isInvalidEmailFormat(userDto.login())) {
      throw new AppException("Invalid email format", HttpStatus.BAD_REQUEST);
    }


    if (userDto.userName().startsWith(" ") || userDto.userName().endsWith(" ") ||
            userDto.login().startsWith(" ") || userDto.login().endsWith(" ")) {
      throw new AppException("Username and login cannot have leading or trailing whitespaces", HttpStatus.BAD_REQUEST);
    }
    else {
      Optional<User> optionalUser = userRepository.findByLogin(userDto.login());

      if (optionalUser.isPresent()) {
        throw new AppException("Login already exists", HttpStatus.BAD_REQUEST);
      }

      User user = userMapper.signUpToUser(userDto);
      user.setPassword(passwordEncoder.encode(CharBuffer.wrap(userDto.password())));

      // Thêm thông tin enable và lưu vào cơ sở dữ liệu
      user.setEnable(0L); // Mặc định là 0
      User savedUser = userRepository.save(user);

      // Gửi email xác nhận
      sendConfirmationEmail(savedUser.getLogin());

      return userMapper.toUserDto(savedUser);
    }
  }

  private void sendConfirmationEmail(String email) {
    String confirmationLink = "http://localhost:8084/confirm?email=" + email; // Thay đổi URL của ứng dụng của bạn
    String emailContent = "Please click the link below to confirm your email:\n" + confirmationLink;

    // Gửi email xác nhận
    emailSender.send(email, emailContent);
  }

  // Trong User_Impl (File F')
  public void confirmEmail(String email) {
    Optional<User> userOptional = userRepository.findByLogin(email);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      user.setEnable(1L); // Set trạng thái enable = 1 khi xác nhận email
      userRepository.save(user);
    } else {
      throw new AppException("User not found for email: " + email, HttpStatus.NOT_FOUND);
    }
  }


  private boolean isInvalidEmailFormat(String email) {
    // Kiểm tra định dạng email
    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    return email == null || !email.matches(emailRegex);
  }
  private boolean isInvalidLength(String input, int maxLength) {
    return input == null || input.length() > maxLength;
  }

  public UserDto findByLogin(String login) {
    User user = userRepository.findByLogin(login)
      .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
    return userMapper.toUserDto(user);
  }

  private void checkNotSpace(String login) {
    if (login.contains(" ")) {
      throw new AppException("Login cannot contain spaces", HttpStatus.BAD_REQUEST);
    }
  }

}
