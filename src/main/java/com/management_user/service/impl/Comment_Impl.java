package com.management_user.service.impl;


import com.management_user.dtos.CommentDTO;
import com.management_user.entity.CommentEntity;
import com.management_user.exceptions.AppException;
import com.management_user.mappers.CommentMapper;
import com.management_user.repository.CommentRepo;
import com.management_user.service.CommentService;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Comment_Impl implements CommentService {
    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private CommentMapper commentMapper;




    @Override
    public List<CommentDTO> getListComments() {
        return commentMapper.toDtos(commentRepo.getAllComments());
    }

    @Override
    public CommentDTO addNewComment(CommentDTO commentDTO){

        CommentEntity commentEntity = new ModelMapper().map(commentDTO,CommentEntity.class);
        commentRepo.save(commentEntity);

        return commentDTO;

    }

    @Override
    @Transactional
    public CommentDTO updateComment(Long id, CommentDTO commentDTO) {
            CommentEntity commentEntity = commentRepo.findById(id).orElse(null);


            commentEntity.setCommentContent(commentDTO.getCommentContent());
            commentEntity.setTime(commentDTO.getTime());
            commentEntity.setStatus(commentDTO.getStatus());
            commentEntity.setCourse(commentEntity.getCourse());
            commentEntity.setStudent(commentEntity.getStudent());
            commentRepo.save(commentEntity);


        return commentDTO;
    }

    @Transactional
    public void deleteCommentById(Long id) {
        // Tìm đối tượng thực thể trong cơ sở dữ liệu
        CommentEntity commentEntity = commentRepo.findById(id)
                .orElseThrow(() -> new NoResultException("Không tìm thấy sản phẩm với ID: " + id));


        // Xóa đối tượng thực thể khỏi cơ sở dữ liệu
        commentRepo.delete(commentEntity);
    }

    public CommentEntity findCommentById(Long id) {
//    return studentRepo.findStudentById(id).orElseThrow(() -> new AppException("Student by id " + id + " was not found",HttpStatus.NOT_FOUND));
        return commentRepo.findById(id).orElseThrow(() -> new AppException("Student by id " + id + " was not found", HttpStatus.NOT_FOUND));
    }

}
