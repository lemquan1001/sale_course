package com.management_user.repository;

import com.management_user.entity.CommentEntity;
import com.management_user.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepo extends JpaRepository<CommentEntity,Long> {
    @Query(value ="SELECT * from comments_entity", nativeQuery = true)
    List<CommentEntity> getAllComments();

//    @Query(value ="SELECT u from comments_entity u where u.id Like = :id", nativeQuery = true)
//    Optional<Object> findCommentById(@Param("id") Long id);
}
