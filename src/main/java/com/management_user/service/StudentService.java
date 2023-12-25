package com.management_user.service;

import com.management_user.dtos.StudentDTO;
import com.management_user.entity.Student;

import java.util.List;

public interface StudentService {
  public List<StudentDTO> getListStudents();
  public StudentDTO addNewStudent(StudentDTO studentDTO);
  public StudentDTO updateStudent(Long id, StudentDTO studentDTO);
  public void deleteStudentById(Long studentId);
  public Student findStudentById(Long id);

//  List<Student> findAllStudent();
//  Student addStudent(Student student);
//  public Student getStudentById(Long id);
//
//  public Student updateStudent(Long id, Student student);
//
//  void deleteStudent(Long id);
}
