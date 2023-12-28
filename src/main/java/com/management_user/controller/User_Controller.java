package com.management_user.controller;

import com.management_user.config.UserAuthProvider;
import com.management_user.dtos.CredentialsDto;
import com.management_user.dtos.SignUpDto;
import com.management_user.dtos.UserDto;
import com.management_user.email.EmailSender;
import com.management_user.entity.User;
import com.management_user.exceptions.AppException;
import com.management_user.repository.UserRepo;
import com.management_user.service.UserService;

import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.CharBuffer;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Map;

@RestController
@RequiredArgsConstructor
//@RequestMapping(path = "/user")
public class User_Controller {

  private final UserService userService;
  private final UserAuthProvider userAuthProvider;
  private final UserRepo userRepo;
  private final PasswordEncoder passwordEncoder;
  private final EmailSender emailSender;
  private static final SecureRandom random = new SecureRandom();
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


  @PostMapping("/forgot-password")
  public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> payload) {
    String login = payload.get("login"); // Sử dụng trường login thay vì email

    if (StringUtils.isBlank(login)) {
      throw new AppException("Login is required", HttpStatus.BAD_REQUEST);
    }

    User user = userRepo.findByLogin(login)
            .orElseThrow(() -> new AppException("User not found for login: " + login, HttpStatus.NOT_FOUND));

    // Tạo token hoặc mật khẩu mới
    String newPassword = generateNewPassword(); // Hàm tạo mật khẩu mới

    // Cập nhật mật khẩu mới trong cơ sở dữ liệu
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepo.save(user);

    // Gửi mật khẩu mới đến email của người dùng (trong trường hợp email là login)
    sendNewPasswordByEmail(login, newPassword);

    return ResponseEntity.ok("New password sent to your email");
  }

  // Thêm hàm để gửi mật khẩu mới đến email
  private void sendNewPasswordByEmail(String email, String newPassword) {
    // Sử dụng dịch vụ gửi email để gửi mật khẩu mới đến email của người dùng
    String emailContent = "Your new password is: " + newPassword;
    emailSender.send(email, emailContent);
  }

  // Hàm tạo mật khẩu mới
  private String generateNewPassword() {
    // Định nghĩa các nhóm ký tự theo yêu cầu
    String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String lowerCase = "abcdefghijklmnopqrstuvwxyz";
    String digits = "0123456789";
    String specialChars = "@$!%*?&";

    // Tất cả các ký tự cho việc tạo mật khẩu
    String allChars = upperCase + lowerCase + digits + specialChars;

    // Sử dụng StringBuilder để xây dựng mật khẩu mới
    StringBuilder newPassword = new StringBuilder();

    // Thêm ít nhất một ký tự từ mỗi nhóm
    newPassword.append(getRandomChar(upperCase));
    newPassword.append(getRandomChar(lowerCase));
    newPassword.append(getRandomChar(digits));
    newPassword.append(getRandomChar(specialChars));

    // Thêm các ký tự ngẫu nhiên từ tất cả các nhóm cho đến khi đạt đến chiều dài yêu cầu
    while (newPassword.length() < 8) {
      newPassword.append(getRandomChar(allChars));
    }

    // Trộn ngẫu nhiên các ký tự trong mật khẩu
    char[] passwordArray = newPassword.toString().toCharArray();
    for (int i = passwordArray.length - 1; i > 0; i--) {
      int index = random.nextInt(i + 1);
      char temp = passwordArray[i];
      passwordArray[i] = passwordArray[index];
      passwordArray[index] = temp;
    }

    return new String(passwordArray);
  }

  private char getRandomChar(String source) {
    int index = random.nextInt(source.length());
    return source.charAt(index);
  }


  @PostMapping("/change-password")
  public ResponseEntity<String> changePassword(@RequestBody Map<String, String> payload) {
    String login = payload.get("login");
    String oldPassword = payload.get("oldPassword");
    String newPassword = payload.get("newPassword");

    if (StringUtils.isAnyBlank(login, oldPassword, newPassword)) {
      throw new AppException("Login, old password, and new password are required", HttpStatus.BAD_REQUEST);
    }

    User user = userRepo.findByLogin(login)
            .orElseThrow(() -> new AppException("User not found for login: " + login, HttpStatus.NOT_FOUND));

    // Kiểm tra mật khẩu cũ
    if (!passwordEncoder.matches(CharBuffer.wrap(oldPassword), user.getPassword())) {
      throw new AppException("Incorrect old password", HttpStatus.BAD_REQUEST);
    }

//    // Kiểm tra độ dài và định dạng của mật khẩu mới
//    if (isInvalidLength(newPassword, 8) || !isValidPasswordFormat(newPassword)) {
//      throw new AppException("Invalid new password format", HttpStatus.BAD_REQUEST);
//    }

    // Cập nhật mật khẩu mới trong cơ sở dữ liệu
    user.setPassword(passwordEncoder.encode(CharBuffer.wrap(newPassword)));
    userRepo.save(user);

    // Gửi thông báo thành công đến người dùng
    return ResponseEntity.ok("Password changed successfully");
  }

  private boolean isInvalidLength(String input, int maxLength) {
    return input == null || input.length() > maxLength;
  }

  private boolean isValidPasswordFormat(String password) {
    // Kiểm tra định dạng mật khẩu
    String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    return password.matches(passwordRegex);
  }
}
