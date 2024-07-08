package com.SWP.WebServer.service;

import com.SWP.WebServer.dto.PackageServiceDTO;
import com.SWP.WebServer.entity.PackageService;
import com.SWP.WebServer.repository.PackageServiceRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PackageServiceService {

    @Autowired
    private PackageServiceRepository packageServiceRepository;

    public List<PackageServiceDTO> getAllPackages() {
        List<PackageService> packages = packageServiceRepository.findAll();
        return packages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PackageServiceDTO getPackageById(Long id) {
        Optional<PackageService> packageOptional = packageServiceRepository.findById(id);
        return packageOptional.map(this::convertToDTO).orElse(null);
    }

    public PackageServiceDTO createPackage(PackageServiceDTO packageServiceDTO) {
        PackageService packageService = convertToEntity(packageServiceDTO);
        PackageService savedPackage = packageServiceRepository.save(packageService);
        return convertToDTO(savedPackage);
    }

    public void deletePackage(Long id) {
        packageServiceRepository.deleteById(id);
    }

    public long countPackages() {
        return packageServiceRepository.count();
    }

    // Helper method to convert Entity to DTO
    private PackageServiceDTO convertToDTO(PackageService packageService) {
        PackageServiceDTO packageServiceDTO = new PackageServiceDTO();
        BeanUtils.copyProperties(packageService, packageServiceDTO);
        return packageServiceDTO;
    }

    // Helper method to convert DTO to Entity
    private PackageService convertToEntity(PackageServiceDTO packageServiceDTO) {
        PackageService packageService = new PackageService();
        BeanUtils.copyProperties(packageServiceDTO, packageService);
        return packageService;
    }

}