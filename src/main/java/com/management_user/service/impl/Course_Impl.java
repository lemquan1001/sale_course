package com.management_user.service.impl;

import com.management_user.dtos.CourseDTO;
import com.management_user.dtos.StudentDTO;
import com.management_user.entity.Course;
import com.management_user.exceptions.AppException;
import com.management_user.mappers.CourseMapper;
import com.management_user.repository.CourseRepo;
import com.management_user.service.CourseService;
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
public class Course_Impl implements CourseService {
    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private CourseMapper courseMapper;




    @Override
    public List<CourseDTO> getListCourses() {

        return courseMapper.toDtos(courseRepo.getAllCourse());
    }

    @Override
    public CourseDTO addNewCourse(CourseDTO courseDTO){
// Kiểm tra nếu bất kỳ trường nào không được nhập hoặc chỉ chứa khoảng trắng hoặc emoji
        if (containsInvalidInput(courseDTO.getName()) ||
                containsInvalidInput(courseDTO.getCategoryName()) ||
//                containsInvalidInput(courseDTO.getDescription()) ||
                containsInvalidInput(String.valueOf(courseDTO.getStatus()))
        ) {
            throw new AppException("Invalid input. Fields cannot be empty or contain emoji.", HttpStatus.BAD_REQUEST);
        }

        // Kiểm tra và giới hạn độ dài của các trường
        if (isInvalidLength(courseDTO.getName(), 255) ||
                isInvalidLength(courseDTO.getCategoryName(), 255) ||
//                isInvalidLength(courseDTO.getDescription(), 50) ||
                isInvalidLength(String.valueOf(courseDTO.getStatus()), 50) ) {
            throw new AppException("Invalid input length", HttpStatus.BAD_REQUEST);
        }

        // Tất cả các trường không có khoảng trắng ở đầu và cuối
        validateInputFields(courseDTO);


        Course course = new ModelMapper().map(courseDTO,Course.class);
        courseRepo.save(course);

        return courseDTO;
    }

    @Override
    @Transactional
    public CourseDTO updateCourse(Long id, CourseDTO courseDTO) {
        Course course = courseRepo.findById(id).orElse(null);


        course.setName(courseDTO.getName());
        course.setCategoryName(courseDTO.getCategoryName());
        course.setDescription(courseDTO.getDescription());
        course.setStatus(courseDTO.getStatus());

        courseRepo.save(course);


        return courseDTO;
    }

    @Transactional
    public void deleteCourseById(Long id) {
        // Tìm đối tượng thực thể trong cơ sở dữ liệu
        Course course = courseRepo.findById(id)
                .orElseThrow(() -> new NoResultException("Không tìm thấy sản phẩm với ID: " + id));


        // Xóa đối tượng thực thể khỏi cơ sở dữ liệu
        courseRepo.delete(course);
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

    private void validateInputFields(CourseDTO courseDTO) {
        if (containsLeadingOrTrailingWhitespace(courseDTO.getName()) ||
                containsLeadingOrTrailingWhitespace(courseDTO.getCategoryName()) ||
                containsLeadingOrTrailingWhitespace(courseDTO.getDescription()) ||
                containsLeadingOrTrailingWhitespace(String.valueOf(courseDTO.getStatus()))) {
            throw new AppException("Input fields cannot have leading or trailing whitespace", HttpStatus.BAD_REQUEST);
        }
    }

    private boolean containsLeadingOrTrailingWhitespace(String value) {
        return value != null && (value.startsWith(" ") || value.endsWith(" "));
    }
}
