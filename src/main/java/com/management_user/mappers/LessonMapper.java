package com.management_user.mappers;

import com.management_user.dtos.LessonDTO;
import com.management_user.entity.Lesson;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LessonMapper{
    Lesson toEntity(LessonDTO lessonDTO);


    LessonDTO toDto(Lesson lesson);

    List<LessonDTO> toDtos(List<Lesson> datas);
}
