package com.management_user.service.impl;


import com.management_user.dtos.TeacherDTO;
import com.management_user.entity.Teacher;
import com.management_user.exceptions.AppException;
import com.management_user.mappers.TeacherMapper;
import com.management_user.repository.TeacherRepo;
import com.management_user.service.TeacherService;
import com.vdurmont.emoji.EmojiParser;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Teacher_Impl implements TeacherService {
    @Autowired
    private TeacherRepo teacherRepo;

    @Autowired
    private TeacherMapper teacherMapper;




    @Override
    public List<TeacherDTO> getListTeachers() {

        return teacherMapper.toDtos(teacherRepo.getAllStudent());
    }

        @Override
    public TeacherDTO addNewTeacher(TeacherDTO teacherDTO){
            // Kiểm tra nếu bất kỳ trường nào không được nhập hoặc chỉ chứa khoảng trắng hoặc emoji
            if (containsInvalidInput(teacherDTO.getName()) ||
                    containsInvalidInput(teacherDTO.getAvatar()) ||
                    containsInvalidInput(teacherDTO.getEmail()) ||
                    containsInvalidInput(teacherDTO.getPassword()) ||
                    containsInvalidInput(teacherDTO.getPhone()) ||
                    containsInvalidInput(teacherDTO.getAddress()) ||
                    containsInvalidInput(teacherDTO.getCreatedAt()) ||
                    containsInvalidInput(teacherDTO.getLastViewAt()) ||
                    containsInvalidInput(String.valueOf(teacherDTO.getCourse()))
            ) {
                throw new AppException("Invalid input. Fields cannot be empty or contain emoji.", HttpStatus.BAD_REQUEST);
            }

            // Kiểm tra và giới hạn độ dài của các trường
            if (isInvalidLength(teacherDTO.getName(), 50) ||
                    isInvalidLength(teacherDTO.getAvatar(), 255) ||
                    isInvalidLength(teacherDTO.getPhone(), 50) ||
                    isInvalidLength(teacherDTO.getAddress(), 50)||
                    isInvalidLength(teacherDTO.getCreatedAt(), 50) ||
                    isInvalidLength(teacherDTO.getLastViewAt(), 50)) {
                throw new AppException("Invalid input length", HttpStatus.BAD_REQUEST);
            }

            // Tất cả các trường không có khoảng trắng ở đầu và cuối
            validateInputFields(teacherDTO);

            // Kiểm tra định dạng password
            if (StringUtils.isBlank(new String(teacherDTO.getPassword())) ||
                    !isInvalidPassword(new String(teacherDTO.getPassword()))) {
                throw new AppException("Invalid password format", HttpStatus.BAD_REQUEST);
            }

            // Kiểm tra định dạng email
            if (isInvalidEmailFormat(teacherDTO.getEmail())) {
                throw new AppException("Invalid email format", HttpStatus.BAD_REQUEST);
            }

            // Check if the email already exists
            if (emailExists(teacherDTO.getEmail())) {
                throw new AppException("Email already exists: " + teacherDTO.getEmail(), HttpStatus.BAD_REQUEST);
            }

            Teacher teacher = new ModelMapper().map(teacherDTO,Teacher.class);
            teacherRepo.save(teacher);

            return teacherDTO;

    }

    @Override
    @Transactional
    public TeacherDTO updateTeacher(Long id,TeacherDTO teacherDTO) {
        Teacher teacher = teacherRepo.findById(id).orElse(null);


        teacher.setName(teacherDTO.getName());
        teacher.setAvatar(teacherDTO.getAvatar());
        teacher.setEmail(teacherDTO.getEmail());
        teacher.setPassword(teacherDTO.getPassword());

        teacher.setPhone(teacherDTO.getPhone());
        teacher.setAddress(teacherDTO.getAddress());
        teacher.setCreatedAt(teacherDTO.getCreatedAt());
        teacher.setLastViewAt(teacherDTO.getLastViewAt());

        teacher.setCourse(teacher.getCourse());
        teacherRepo.save(teacher);


        return teacherDTO;
    }

    @Transactional
    public void deleteTeacherById(Long id) {
        // Tìm đối tượng thực thể trong cơ sở dữ liệu
        Teacher teacher = teacherRepo.findById(id)
                .orElseThrow(() -> new NoResultException("Không tìm thấy sản phẩm với ID: " + id));


        // Xóa đối tượng thực thể khỏi cơ sở dữ liệu
        teacherRepo.delete(teacher);
    }

    private boolean emailExists(String email) {
        return teacherRepo.existsByEmail(email);
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

    private void validateInputFields(TeacherDTO teacherDTO) {
        if (containsLeadingOrTrailingWhitespace(teacherDTO.getName()) ||
                containsLeadingOrTrailingWhitespace(teacherDTO.getAvatar()) ||
                containsLeadingOrTrailingWhitespace(teacherDTO.getEmail()) ||
                containsLeadingOrTrailingWhitespace(teacherDTO.getPassword()) ||
                containsLeadingOrTrailingWhitespace(teacherDTO.getPhone()) ||

                containsLeadingOrTrailingWhitespace(teacherDTO.getAddress()) ||
                containsLeadingOrTrailingWhitespace(teacherDTO.getCreatedAt()) ||
                containsLeadingOrTrailingWhitespace(teacherDTO.getLastViewAt()) ||
                containsLeadingOrTrailingWhitespace(String.valueOf(teacherDTO.getCourse()))
        ) {
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
