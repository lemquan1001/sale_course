package com.management_user.service;

import com.management_user.dtos.LessonDTO;

import java.util.List;

public interface LessonService {
    public List<LessonDTO> getListLessons();
    public LessonDTO addNewLesson(LessonDTO lessonDTO);
    public LessonDTO updateLesson(Long id, LessonDTO lessonDTO);
    public void deleteLessonById(Long id);
}
