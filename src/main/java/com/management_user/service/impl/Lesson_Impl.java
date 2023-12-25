package com.management_user.service.impl;

import com.management_user.dtos.LessonDTO;
import com.management_user.entity.Lesson;
import com.management_user.mappers.LessonMapper;
import com.management_user.repository.LessonRepo;
import com.management_user.service.LessonService;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class Lesson_Impl implements LessonService {
    @Autowired
    private LessonRepo lessonRepo;

    @Autowired
    private LessonMapper lessonMapper;




    @Override
    public List<LessonDTO> getListLessons() {

        return lessonMapper.toDtos(lessonRepo.getAllLessons());
    }

    @Override
    public LessonDTO addNewLesson(LessonDTO lessonDTO){
//
//        Course course = courseMapper.toEntity(courseDTO);
//
//        course.setName(courseDTO.getName());
//        course.setCategoryName(courseDTO.getCategoryName());
//        course.setDescription(courseDTO.getDescription());
//        course.setStatus(courseDTO.getStatus());
//
//        course.setTeacher(courseDTO.getTeacher());
//
//        return courseMapper.toDto(courseRepo.save(course));
        Lesson lesson = new ModelMapper().map(lessonDTO,Lesson.class);
        lessonRepo.save(lesson);

        return lessonDTO;
    }

    @Override
    @Transactional
    public LessonDTO updateLesson(Long id, LessonDTO lessonDTO) {
        Lesson lesson = lessonRepo.findById(id).orElse(null);


        lesson.setName(lessonDTO.getName());
        lesson.setDescription(lessonDTO.getDescription());
        lesson.setContent(lessonDTO.getContent());
        lesson.setDisplayIndex(lessonDTO.getDisplayIndex());
        lesson.setStatus(lessonDTO.getStatus());
        lesson.setCourse(lesson.getCourse());

        lessonRepo.save(lesson);

        return lessonDTO;
    }

    @Transactional
    public void deleteLessonById(Long id) {
        // Tìm đối tượng thực thể trong cơ sở dữ liệu
        Lesson lesson = lessonRepo.findById(id)
                .orElseThrow(() -> new NoResultException("Không tìm thấy sản phẩm với ID: " + id));


        // Xóa đối tượng thực thể khỏi cơ sở dữ liệu
        lessonRepo.delete(lesson);
    }
}
