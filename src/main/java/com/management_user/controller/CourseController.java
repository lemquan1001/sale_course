package com.management_user.controller;

import com.management_user.base.BaseResponse;
import com.management_user.constants.StatusCode;
import com.management_user.dtos.CourseDTO;
import com.management_user.dtos.TeacherDTO;
import com.management_user.service.CourseService;
import com.management_user.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/course")
public class CourseController {
    @Autowired
    private CourseService courseService;


    @GetMapping("/getAll")
    public ResponseEntity getAllCourse() {
        return ResponseEntity.ok(new BaseResponse(courseService.getListCourses(),"Thành công", StatusCode.SUCCESS));
    }

    @PostMapping("/add")
    ResponseEntity addNewCourse(@RequestBody CourseDTO courseDTO){
        return ResponseEntity.ok(new BaseResponse(courseService.addNewCourse(courseDTO),"Thêm mới sản phẩm thành công", StatusCode.SUCCESS));
    }

    @PutMapping("/updateCourse/{id}")
    @ResponseBody
    ResponseEntity updateCourse(@PathVariable(name="id") Long id,@RequestBody CourseDTO courseDTO){
        return ResponseEntity.ok(new BaseResponse(courseService.updateCourse(id,courseDTO),"Cập nhật sản phẩm thành công",StatusCode.SUCCESS));
    }


    @DeleteMapping("/deleteCourse/{id}")
    ResponseEntity deleteCourse(@PathVariable(name = "id") Long id){
        courseService.deleteCourseById(id);
        //return new ResponseEntity<>(HttpStatus.OK);
        return  ResponseEntity.ok(new BaseResponse(null,"Xóa sản phẩm thành công",StatusCode.SUCCESS));
    }
}
