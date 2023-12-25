package com.management_user.service.impl;

import com.management_user.dtos.StudentDTO;
import com.management_user.entity.Student;
import com.management_user.exceptions.AppException;
import com.management_user.mappers.StudentMapper;
import com.management_user.repository.StudentRepo;
import com.management_user.service.StudentService;
import com.vdurmont.emoji.EmojiParser;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Student_Impl implements StudentService {

  @Autowired
  private StudentRepo studentRepo;

  @Autowired
  private StudentMapper studentMapper;



  @Override
  public List<StudentDTO> getListStudents() {

    return studentMapper.toDtos(studentRepo.getAllStudent());
  }

  @Override
  public StudentDTO addNewStudent(StudentDTO studentDTO) {
    // Kiểm tra nếu bất kỳ trường nào không được nhập hoặc chỉ chứa khoảng trắng hoặc emoji
    if (containsInvalidInput(studentDTO.getEmail()) ||
            containsInvalidInput(studentDTO.getPassword()) ||
            containsInvalidInput(studentDTO.getAddress()) ||
            containsInvalidInput(studentDTO.getName()) ||
            containsInvalidInput(studentDTO.getPhone()) ||
            containsInvalidInput(String.valueOf(studentDTO.getPaymentId())) ||
            containsInvalidInput(studentDTO.getCreatedAt()) ||
            containsInvalidInput(studentDTO.getLastViewAt())
            ) {
      throw new AppException("Invalid input. Fields cannot be empty or contain emoji.", HttpStatus.BAD_REQUEST);
    }

    // Kiểm tra và giới hạn độ dài của các trường
    if (isInvalidLength(studentDTO.getAddress(), 50) ||
            isInvalidLength(studentDTO.getName(), 50) ||
            isInvalidLength(studentDTO.getPhone(), 50) ||
            isInvalidLength(String.valueOf(studentDTO.getPaymentId()), 50)||
            isInvalidLength(studentDTO.getCreatedAt(), 50) ||
            isInvalidLength(studentDTO.getLastViewAt(), 50)) {
      throw new AppException("Invalid input length", HttpStatus.BAD_REQUEST);
    }

    // Tất cả các trường không có khoảng trắng ở đầu và cuối
    validateInputFields(studentDTO);

    // Kiểm tra định dạng password
    if (StringUtils.isBlank(new String(studentDTO.getPassword())) ||
            !isInvalidPassword(new String(studentDTO.getPassword()))) {
      throw new AppException("Invalid password format", HttpStatus.BAD_REQUEST);
    }

    // Kiểm tra định dạng email
    if (isInvalidEmailFormat(studentDTO.getEmail())) {
      throw new AppException("Invalid email format", HttpStatus.BAD_REQUEST);
    }


    // Check if the email already exists
    if (emailExists(studentDTO.getEmail())) {
      throw new AppException("Email already exists: " + studentDTO.getEmail(), HttpStatus.BAD_REQUEST);
    }

    // If email does not exist, proceed with adding the new student
    Student student = studentMapper.toEntity(studentDTO);

    student.setEmail(studentDTO.getEmail());
    student.setPassword(studentDTO.getPassword());
    student.setName(studentDTO.getName());
    student.setPhone(studentDTO.getPhone());
    student.setAddress(studentDTO.getAddress());
    student.setPaymentId(studentDTO.getPaymentId());
    student.setCreatedAt(studentDTO.getCreatedAt());
    student.setLastViewAt(studentDTO.getLastViewAt());

    return studentMapper.toDto(studentRepo.save(student));
  }


  @Override
  @Transactional
  public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
    Student student = studentRepo.findById(id).orElse(null);


    student.setEmail(studentDTO.getEmail());
    student.setPassword(studentDTO.getPassword());
    student.setName(studentDTO.getName());
    student.setPhone(studentDTO.getPhone());

    student.setAddress(studentDTO.getAddress());
    student.setPaymentId(studentDTO.getPaymentId());
    student.setCreatedAt(studentDTO.getCreatedAt());
    student.setLastViewAt(studentDTO.getLastViewAt());

    studentRepo.save(student);


    return studentDTO;
  }

  @Transactional
  public void deleteStudentById(Long studentId) {
    // Tìm đối tượng thực thể trong cơ sở dữ liệu
    Student student = studentRepo.findById(studentId)
      .orElseThrow(() -> new NoResultException("Không tìm thấy sản phẩm với ID: " + studentId));


    // Xóa đối tượng thực thể khỏi cơ sở dữ liệu
    studentRepo.delete(student);
  }


  public Student findStudentById(Long id) {
//    return studentRepo.findStudentById(id).orElseThrow(() -> new AppException("Student by id " + id + " was not found",HttpStatus.NOT_FOUND));
    return studentRepo.findById(id).orElseThrow(() -> new AppException("Student by id " + id + " was not found",HttpStatus.NOT_FOUND));
  }


  // Helper method to check if email exists
  private boolean emailExists(String email) {
    return studentRepo.existsByEmail(email);
  }

  private boolean containsInvalidInput(String input) {
    return StringUtils.isBlank(input) || containsEmoji(input.toCharArray());
  }

  private boolean containsEmoji(char[] input) {
    String text = new String(input);
    return !EmojiParser.extractEmojis(text).isEmpty();
  }

  private boolean isInvalidLength(String input, int maxLength) {
    return input == null || input.length() > maxLength;
  }

  private void validateInputFields(StudentDTO studentDTO) {
    if (containsLeadingOrTrailingWhitespace(studentDTO.getEmail()) ||
            containsLeadingOrTrailingWhitespace(studentDTO.getPassword()) ||
            containsLeadingOrTrailingWhitespace(studentDTO.getName()) ||
            containsLeadingOrTrailingWhitespace(studentDTO.getPhone()) ||
            containsLeadingOrTrailingWhitespace(studentDTO.getAddress())) {
      throw new AppException("Input fields cannot have leading or trailing whitespace", HttpStatus.BAD_REQUEST);
    }
  }

  private boolean containsLeadingOrTrailingWhitespace(String value) {
    return value != null && (value.startsWith(" ") || value.endsWith(" "));
  }

  private boolean isInvalidPassword(String password) {
    // Kiểm tra độ dài tối thiểu, chữ cái viết hoa, chữ cái viết thường, chữ số, ký tự đặc biệt, không chứa khoảng trắng
    String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    return password.matches(passwordRegex) && !password.contains(" ");
  }

  private boolean isInvalidEmailFormat(String email) {
    // Kiểm tra định dạng email
    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    return email == null || !email.matches(emailRegex);
  }
}
