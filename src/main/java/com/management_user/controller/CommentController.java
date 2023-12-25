package com.management_user.controller;


import com.management_user.base.BaseResponse;
import com.management_user.constants.StatusCode;
import com.management_user.dtos.CommentDTO;
import com.management_user.dtos.TeacherDTO;
import com.management_user.entity.CommentEntity;
import com.management_user.entity.Student;
import com.management_user.service.CommentService;
import com.management_user.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/comment")
@RequiredArgsConstructor
public class CommentController {

    @Autowired
    private CommentService commentService;


    @GetMapping("/getAll")
    public ResponseEntity getAllComment() {
        return ResponseEntity.ok(new BaseResponse(commentService.getListComments(),"Thành công", StatusCode.SUCCESS));
    }
    @GetMapping("/findCommentById/{id}")
    public ResponseEntity<CommentEntity> getCommentById(@PathVariable Long id){
//        CommentEntity commentEntity = commentService.findCommentById(id);
//        return  new ResponseEntity<>(commentEntity, HttpStatus.OK);
        CommentEntity commentEntity = commentService.findCommentById(id);
        if (commentEntity != null) {
            return new ResponseEntity<>(commentEntity, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add")
    ResponseEntity addNewComment(@RequestBody CommentDTO commentDTO){
        return ResponseEntity.ok(new BaseResponse(commentService.addNewComment(commentDTO),"Thêm mới sản phẩm thành công", StatusCode.SUCCESS));
    }

    @PutMapping("/updateComment/{id}")
    @ResponseBody
    ResponseEntity updateComment(@PathVariable(name = "id") Long id,@RequestBody CommentDTO commentDTO){
        return ResponseEntity.ok(new BaseResponse(commentService.updateComment(id,commentDTO),"Cập nhật sản phẩm thành công",StatusCode.SUCCESS));
    }


    @DeleteMapping("/deleteComment/{id}")
    ResponseEntity deleteComment(@PathVariable(name = "id") Long id){
        commentService.deleteCommentById(id);
        //return new ResponseEntity<>(HttpStatus.OK);
        return  ResponseEntity.ok(new BaseResponse(null,"Xóa sản phẩm thành công",StatusCode.SUCCESS));
    }
}
