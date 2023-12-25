package com.management_user.controller;

import com.management_user.base.BaseResponse;
import com.management_user.constants.StatusCode;
import com.management_user.dtos.StudentDTO;
import com.management_user.entity.Student;
import com.management_user.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/student")
@RequiredArgsConstructor
public class Student_Controller {

  @Autowired
  private StudentService studentService;


  @GetMapping("/getAll")
  public ResponseEntity getAllStudent() {
    return ResponseEntity.ok(new BaseResponse(studentService.getListStudents(),"Thành công", StatusCode.SUCCESS));
  }

  @PostMapping("/add")
  @ResponseBody
  ResponseEntity addNewStudent(@RequestBody StudentDTO studentDTO){
    return ResponseEntity.ok(new BaseResponse(studentService.addNewStudent(studentDTO),"Thêm mới sản phẩm thành công", StatusCode.SUCCESS));
  }

  @PutMapping("/updateStudent/{id}")
  @ResponseBody
  ResponseEntity updateStudent(@PathVariable Long id,@RequestBody StudentDTO studentDTO){
    return ResponseEntity.ok(new BaseResponse(studentService.updateStudent(id,studentDTO),"Cập nhật sản phẩm thành công",StatusCode.SUCCESS));
  }


  @DeleteMapping("/deleteStudent/{id}")
  ResponseEntity deleteStudent(@PathVariable(name = "id") Long id){
    studentService.deleteStudentById(id);
    //return new ResponseEntity<>(HttpStatus.OK);
    return  ResponseEntity.ok(new BaseResponse(null,"Xóa sản phẩm thành công",StatusCode.SUCCESS));
  }


  @GetMapping("/findById/{id}")
  public ResponseEntity<Student> getStudentById(@PathVariable("id") Long id){
    Student student = studentService.findStudentById(id);
    return  new ResponseEntity<>(student, HttpStatus.OK);
  }
}
