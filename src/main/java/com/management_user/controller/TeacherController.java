package com.management_user.controller;


import com.management_user.base.BaseResponse;
import com.management_user.constants.StatusCode;
import com.management_user.dtos.ResponseDTO;
import com.management_user.dtos.StudentDTO;
import com.management_user.dtos.TeacherDTO;
import com.management_user.dtos.UserDto;
import com.management_user.service.StudentService;
import com.management_user.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping(path = "/teacher")
@RequiredArgsConstructor
public class TeacherController {
    @Autowired
    private TeacherService teacherService;


    @GetMapping("/getAll")
    public ResponseEntity getAllTeacher() {
        return ResponseEntity.ok(new BaseResponse(teacherService.getListTeachers(),"Thành công", StatusCode.SUCCESS));
    }

    @PostMapping("/add")
    ResponseEntity addNewTeacher(@RequestBody TeacherDTO teacherDTO){
        return ResponseEntity.ok(new BaseResponse(teacherService.addNewTeacher(teacherDTO),"Thêm mới sản phẩm thành công", StatusCode.SUCCESS));
    }

    @PutMapping("/updateTeacher/{id}")
    @ResponseBody
    ResponseEntity updateTeacher(@PathVariable(name = "id") Long id,@RequestBody TeacherDTO teacherDTO){
        return ResponseEntity.ok(new BaseResponse(teacherService.updateTeacher(id,teacherDTO),"Cập nhật sản phẩm thành công",StatusCode.SUCCESS));
    }


    @DeleteMapping("/deleteTeacher/{id}")
    ResponseEntity deleteTeacher(@PathVariable(name = "id") Long id){
        teacherService.deleteTeacherById(id);
        //return new ResponseEntity<>(HttpStatus.OK);
        return  ResponseEntity.ok(new BaseResponse(null,"Xóa sản phẩm thành công",StatusCode.SUCCESS));
    }
}
