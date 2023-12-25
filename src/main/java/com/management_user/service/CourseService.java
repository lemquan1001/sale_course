package com.management_user.service;

import com.management_user.dtos.CourseDTO;

import java.util.List;

public interface CourseService {
    public List<CourseDTO> getListCourses();
    public CourseDTO addNewCourse(CourseDTO courseDTO);
    public CourseDTO updateCourse(Long id, CourseDTO courseDTO);

    public void deleteCourseById(Long id);
}
