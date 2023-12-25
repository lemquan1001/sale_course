package com.management_user.mappers;


import com.management_user.dtos.PackageDTO;
import com.management_user.entity.PackageEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PackageMapper {
    PackageEntity toEntity(PackageDTO packageDTO);


    PackageDTO toDto(PackageEntity packageEntity);

    List<PackageDTO> toDtos(List<PackageEntity> datas);
}
