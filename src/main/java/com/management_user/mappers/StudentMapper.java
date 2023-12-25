package com.management_user.mappers;


import com.management_user.dtos.StudentDTO;
import com.management_user.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper {

  Student toEntity(StudentDTO studentDTO);


  StudentDTO toDto(Student student);

  List<StudentDTO> toDtos(List<Student> datas);
}
