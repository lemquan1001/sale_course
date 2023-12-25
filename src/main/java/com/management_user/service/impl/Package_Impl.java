package com.management_user.service.impl;

import com.management_user.dtos.LessonDTO;
import com.management_user.dtos.PackageDTO;
import com.management_user.entity.Lesson;
import com.management_user.entity.PackageEntity;
import com.management_user.mappers.LessonMapper;
import com.management_user.mappers.PackageMapper;
import com.management_user.repository.LessonRepo;
import com.management_user.repository.PackageRepo;
import com.management_user.service.PackageService;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Package_Impl implements PackageService {
    @Autowired
    private PackageRepo packageRepo;

    @Autowired
    private PackageMapper packageMapper;




    @Override
    public List<PackageDTO> getListPackages() {

        return packageMapper.toDtos(packageRepo.getAllLessons());
    }

    @Override
    public PackageDTO addNewPackage(PackageDTO packageDTO){
//
//        Course course = courseMapper.toEntity(courseDTO);
//
//        course.setName(courseDTO.getName());
//        course.setCategoryName(courseDTO.getCategoryName());
//        course.setDescription(courseDTO.getDescription());
//        course.setStatus(courseDTO.getStatus());
//
//        course.setTeacher(courseDTO.getTeacher());
//
//        return courseMapper.toDto(courseRepo.save(course));
        PackageEntity packageEntity = new ModelMapper().map(packageDTO,PackageEntity.class);
        packageRepo.save(packageEntity);

        return packageDTO;
    }

    @Override
    @Transactional
    public PackageDTO updatePackage(Long id, PackageDTO packageDTO) {
        PackageEntity packageEntity = packageRepo.findById(id).orElse(null);


        packageEntity.setPackageName(packageDTO.getPackageName());
        packageEntity.setPackageCode(packageDTO.getPackageCode());
        packageEntity.setDescription(packageDTO.getDescription());
        packageEntity.setPrice(packageDTO.getPrice());

        packageRepo.save(packageEntity);

        return packageDTO;
    }

    @Transactional
    public void deletePackageById(Long id) {
        // Tìm đối tượng thực thể trong cơ sở dữ liệu
        PackageEntity packageEntity = packageRepo.findById(id)
                .orElseThrow(() -> new NoResultException("Không tìm thấy sản phẩm với ID: " + id));


        // Xóa đối tượng thực thể khỏi cơ sở dữ liệu
        packageRepo.delete(packageEntity);
    }
}
