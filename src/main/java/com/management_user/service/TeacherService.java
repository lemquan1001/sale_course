package com.management_user.service;

import com.management_user.dtos.TeacherDTO;

import java.util.List;

public interface TeacherService {
    public List<TeacherDTO> getListTeachers();
    public TeacherDTO addNewTeacher(TeacherDTO teacherDTO);
    public TeacherDTO updateTeacher(Long id,TeacherDTO teacherDTO);
    public void deleteTeacherById(Long id);

}
