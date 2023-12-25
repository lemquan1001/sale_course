package com.management_user.repository;

import com.management_user.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeacherRepo extends JpaRepository<Teacher,Long> {
    @Query(value ="SELECT * from teachers", nativeQuery = true)
    List<Teacher> getAllStudent();

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Teacher s WHERE s.email = :email")
    boolean existsByEmail(@Param("email") String email);
}
