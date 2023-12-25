package com.management_user.repository;

import com.management_user.entity.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PackageRepo extends JpaRepository<PackageEntity,Long> {
    @Query(value ="SELECT * from packages_entity", nativeQuery = true)
    List<PackageEntity> getAllLessons();
}
