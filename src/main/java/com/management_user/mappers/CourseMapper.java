package com.management_user.mappers;

import com.management_user.dtos.CourseDTO;
import com.management_user.entity.Course;
import org.mapstruct.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper{
    Course toEntity(CourseDTO courseDTO);


    CourseDTO toDto(Course course);

    List<CourseDTO> toDtos(List<Course> datas);
}
