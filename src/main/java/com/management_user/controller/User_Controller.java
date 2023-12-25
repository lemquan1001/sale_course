package com.management_user.controller;

import com.management_user.config.UserAuthProvider;
import com.management_user.dtos.CredentialsDto;
import com.management_user.dtos.SignUpDto;
import com.management_user.dtos.UserDto;
import com.management_user.exceptions.AppException;
import com.management_user.service.UserService;

import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Arrays;

@RestController
@RequiredArgsConstructor
//@RequestMapping(path = "/user")
public class User_Controller {

  private final UserService userService;
  private final UserAuthProvider userAuthProvider;
  @PostMapping("/login")
  public ResponseEntity<UserDto> login(@RequestBody CredentialsDto credentialsDto) {
    UserDto user = userService.login(credentialsDto);
    user.setToken(userAuthProvider.createToken(user));
    return ResponseEntity.ok(user);
  }



  @PostMapping("/register")
  public ResponseEntity<UserDto> login(@RequestBody SignUpDto signUpDto) {
//    if (StringUtils.isBlank(new String(signUpDto.password()))) {
//      throw new AppException("Please provide all required fields", HttpStatus.BAD_REQUEST);
//    }
//    UserDto createdUser = userService.register(signUpDto);
//    createdUser.setToken(userAuthProvider.createToken(createdUser));
//    return ResponseEntity.created(URI.create("users" + createdUser.getId())).body(createdUser);


    // Kiểm tra password
    if (StringUtils.isBlank(new String(signUpDto.password())) ||
            !isInvalidPassword(new String(signUpDto.password()))) {
      throw new AppException("Invalid password format", HttpStatus.BAD_REQUEST);
    }

    //kiếm tra password không được bỏ trống
    if (StringUtils.isBlank(new String(signUpDto.password()))) {
      throw new AppException("Please provide all required fields", HttpStatus.BAD_REQUEST);
    }
    // Kiểm tra nếu bất kỳ trường nào không được nhập hoặc chỉ chứa khoảng trắng hoặc emoji
    if (containsInvalidInput(signUpDto.userName()) ||
            containsInvalidInput(signUpDto.login()) ||
            containsInvalidInput(Arrays.toString(signUpDto.password()))) {
      throw new AppException("Invalid input. Fields cannot be empty or contain emoji.", HttpStatus.BAD_REQUEST);
    }

    UserDto createdUser = userService.register(signUpDto);
    createdUser.setToken(userAuthProvider.createToken(createdUser));
    return ResponseEntity.created(URI.create("users/" + createdUser.getId())).body(createdUser);
  }

  @GetMapping("/confirm")
  public ResponseEntity<String> confirmEmail(@RequestParam("email") String email) {
    // Thực hiện xác nhận email, set trạng thái enable = 1
    userService.confirmEmail(email);

    return ResponseEntity.ok("Email confirmed successfully!");
  }

  private boolean isInvalidPassword(String password) {
    // Kiểm tra độ dài tối thiểu, chữ cái viết hoa, chữ cái viết thường, chữ số, ký tự đặc biệt, không chứa khoảng trắng
    String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    return password.matches(passwordRegex) && !password.contains(" ");
  }
  private boolean containsInvalidInput(String input) {
    return StringUtils.isBlank(input) || containsEmoji(input.toCharArray());
  }

  private boolean containsEmoji(char[] input) {
    String text = new String(input);
    return !EmojiParser.extractEmojis(text).isEmpty();
  }

}
