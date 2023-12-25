package com.management_user.controller;


import com.management_user.base.BaseResponse;
import com.management_user.constants.StatusCode;
import com.management_user.dtos.LessonDTO;
import com.management_user.dtos.PackageDTO;
import com.management_user.service.LessonService;
import com.management_user.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/package")
public class PackageController {
    @Autowired
    private PackageService packageService;


    @GetMapping("/getAll")
    public ResponseEntity getAllPackages() {
        return ResponseEntity.ok(new BaseResponse(packageService.getListPackages(),"Thành công", StatusCode.SUCCESS));
    }

    @PostMapping("/add")
    ResponseEntity addNewPackage(@RequestBody PackageDTO packageDTO){
        return ResponseEntity.ok(new BaseResponse(packageService.addNewPackage(packageDTO),"Thêm mới sản phẩm thành công", StatusCode.SUCCESS));
    }

    @PutMapping("/updatePackage/{id}")
    @ResponseBody
    ResponseEntity updatePackage(@PathVariable(name = "id") Long id,@RequestBody PackageDTO packageDTO){
        return ResponseEntity.ok(new BaseResponse(packageService.updatePackage(id,packageDTO),"Cập nhật sản phẩm thành công",StatusCode.SUCCESS));
    }


    @DeleteMapping("/deletePackage/{id}")
    ResponseEntity deletePackage(@PathVariable(name = "id") Long id){
        packageService.deletePackageById(id);
        //return new ResponseEntity<>(HttpStatus.OK);
        return  ResponseEntity.ok(new BaseResponse(null,"Xóa sản phẩm thành công",StatusCode.SUCCESS));
    }
}
