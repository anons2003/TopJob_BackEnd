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

    public PackageServiceDTO getPackageById(int id) {
        Optional<PackageService> packageOptional = packageServiceRepository.findById(id);
        return packageOptional.map(this::convertToDTO).orElse(null);
    }

    public PackageServiceDTO createPackage(PackageServiceDTO packageServiceDTO) {
        PackageService packageService = convertToEntity(packageServiceDTO);
        PackageService savedPackage = packageServiceRepository.save(packageService);
        return convertToDTO(savedPackage);
    }

    public void deletePackage(int id) {
        packageServiceRepository.deleteById(id);
    }

    public long countPackages() {
        return packageServiceRepository.count();
    }

    public Optional<PackageServiceDTO> updatePackage(int id, PackageServiceDTO packageServiceDTO) {
        Optional<PackageService> existingPackage = packageServiceRepository.findById(id);
        if (existingPackage.isPresent()) {
            PackageService packageService = existingPackage.get();
            BeanUtils.copyProperties(packageServiceDTO, packageService);
            PackageService updatedPackage = packageServiceRepository.save(packageService);
            return Optional.of(convertToDTO(updatedPackage));
        } else {
            return Optional.empty();
        }
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
