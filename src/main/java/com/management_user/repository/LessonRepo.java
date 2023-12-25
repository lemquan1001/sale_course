package com.management_user.repository;

import com.management_user.entity.Course;
import com.management_user.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LessonRepo extends JpaRepository<Lesson,Long> {
    @Query(value ="SELECT * from lessons", nativeQuery = true)
    List<Lesson> getAllLessons();
}
