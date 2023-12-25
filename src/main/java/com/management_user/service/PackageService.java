package com.management_user.service;

import com.management_user.dtos.PackageDTO;

import java.util.List;

public interface PackageService {
    public List<PackageDTO> getListPackages();
    public PackageDTO addNewPackage(PackageDTO packageDTO);
    public PackageDTO updatePackage(Long id, PackageDTO packageDTO);
    public void deletePackageById(Long id);
}
