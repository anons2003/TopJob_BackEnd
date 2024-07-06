package com.SWP.WebServer.controller;

import com.SWP.WebServer.dto.PackageServiceDTO;
import com.SWP.WebServer.service.PackageServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/packages")
public class PackageServiceController {

    @Autowired
    private PackageServiceService packageServiceService;

    @GetMapping
    public ResponseEntity<List<PackageServiceDTO>> getAllPackages() {
        List<PackageServiceDTO> packages = packageServiceService.getAllPackages();
        return new ResponseEntity<>(packages, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PackageServiceDTO> getPackageById(@PathVariable("id") Long id) {
        PackageServiceDTO packageService = packageServiceService.getPackageById(id);
        if (packageService != null) {
            return new ResponseEntity<>(packageService, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<PackageServiceDTO> createPackage(@RequestBody PackageServiceDTO packageServiceDTO) {
        PackageServiceDTO createdPackage = packageServiceService.createPackage(packageServiceDTO);
        return new ResponseEntity<>(createdPackage, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePackage(@PathVariable("id") Long id) {
        packageServiceService.deletePackage(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/totalPackageService")
    public ResponseEntity<Long> countPackages() {
        long count = packageServiceService.countPackages();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    // Các phương thức khác tùy theo yêu cầu
}
