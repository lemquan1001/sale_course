package com.management_user.mappers;

import com.management_user.dtos.CommentDTO;
import com.management_user.entity.CommentEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentEntity toEntity(CommentDTO commentDTO);


    CommentDTO toDto(CommentEntity commentEntity);

    List<CommentDTO> toDtos(List<CommentEntity> datas);
}
