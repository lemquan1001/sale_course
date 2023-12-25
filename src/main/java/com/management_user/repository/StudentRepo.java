package com.management_user.repository;

import com.management_user.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepo extends JpaRepository<Student,Long> {
  @Query(value ="SELECT * from students", nativeQuery = true)
  List<Student> getAllStudent();


  @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Student s WHERE s.email = :email")
  boolean existsByEmail(@Param("email") String email);

//  @Query("SELECT b FROM Student b WHERE b.id = :id")
//  Optional<Object> findStudentById(@Param("id") Long id);
}
