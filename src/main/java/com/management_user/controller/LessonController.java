package com.management_user.controller;

import com.management_user.base.BaseResponse;
import com.management_user.constants.StatusCode;
import com.management_user.dtos.CourseDTO;
import com.management_user.dtos.LessonDTO;
import com.management_user.service.CourseService;
import com.management_user.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/lesson")
public class LessonController {
    @Autowired
    private LessonService lessonService;


    @GetMapping("/getAll")
    public ResponseEntity getAllLessons() {
        return ResponseEntity.ok(new BaseResponse(lessonService.getListLessons(),"Thành công", StatusCode.SUCCESS));
    }

    @PostMapping("/add")
    ResponseEntity addNewLesson(@RequestBody LessonDTO lessonDTO){
        return ResponseEntity.ok(new BaseResponse(lessonService.addNewLesson(lessonDTO),"Thêm mới sản phẩm thành công", StatusCode.SUCCESS));
    }

    @PutMapping("/updateLesson/{id}")
    @ResponseBody
    ResponseEntity updateLesson(@PathVariable(name = "id") Long id,@RequestBody LessonDTO lessonDTO){
        return ResponseEntity.ok(new BaseResponse(lessonService.updateLesson(id,lessonDTO),"Cập nhật sản phẩm thành công",StatusCode.SUCCESS));
    }


    @DeleteMapping("/deleteLesson/{id}")
    ResponseEntity deleteLesson(@PathVariable(name = "id") Long id){
        lessonService.deleteLessonById(id);
        //return new ResponseEntity<>(HttpStatus.OK);
        return  ResponseEntity.ok(new BaseResponse(null,"Xóa sản phẩm thành công",StatusCode.SUCCESS));
    }
}
