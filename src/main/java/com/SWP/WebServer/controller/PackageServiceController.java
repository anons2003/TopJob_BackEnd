package com.SWP.WebServer.controller;

import com.SWP.WebServer.dto.PackageServiceDTO;
import com.SWP.WebServer.service.PackageServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/packageServices")
public class PackageServiceController {

    @Autowired
    private PackageServiceService packageServiceService;

    @GetMapping
    public ResponseEntity<List<PackageServiceDTO>> getAllPackages() {
        List<PackageServiceDTO> packages = packageServiceService.getAllPackages();
        return new ResponseEntity<>(packages, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PackageServiceDTO> getPackageById(@PathVariable("id") int id) {
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

    @GetMapping("/totalPackageService")
    public ResponseEntity<Long> countPackages() {
        long count = packageServiceService.countPackages();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
    @GetMapping("/list")
    public ResponseEntity<List<PackageServiceDTO>> getAllPackagesList() {
        List<PackageServiceDTO> packages = packageServiceService.getAllPackages();
        return ResponseEntity.ok().body(packages);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PackageServiceDTO> updatePackage(@PathVariable("id") int id, @RequestBody PackageServiceDTO packageServiceDTO) {
        Optional<PackageServiceDTO> updatedPackage = packageServiceService.updatePackage(id, packageServiceDTO);
        if (updatedPackage.isPresent()) {
            return ResponseEntity.ok(updatedPackage.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePackage(@PathVariable("id") int id) {
        packageServiceService.deletePackage(id);
        return ResponseEntity.noContent().build();
    }
}