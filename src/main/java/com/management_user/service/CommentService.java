package com.management_user.service;

import com.management_user.dtos.CommentDTO;
import com.management_user.entity.CommentEntity;

import java.util.List;

public interface CommentService {
    public List<CommentDTO> getListComments();
    public CommentDTO addNewComment(CommentDTO commentDTO);
    public CommentDTO updateComment(Long id, CommentDTO commentDTO);
    public void deleteCommentById(Long id);
    public CommentEntity findCommentById(Long id);
}
