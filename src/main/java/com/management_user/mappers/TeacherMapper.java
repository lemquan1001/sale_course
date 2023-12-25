package com.management_user.mappers;

import com.management_user.dtos.TeacherDTO;
import com.management_user.entity.Teacher;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TeacherMapper {
    Teacher toEntity(TeacherDTO teacherDTO);


    TeacherDTO toDto(Teacher teacher);

    List<TeacherDTO> toDtos(List<Teacher> datas);
}
