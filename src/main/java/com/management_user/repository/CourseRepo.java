package com.management_user.repository;

import com.management_user.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepo extends JpaRepository<Course,Long> {
    @Query(value ="SELECT * from courses", nativeQuery = true)
    List<Course> getAllCourse();
}
