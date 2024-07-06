package com.SWP.WebServer.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "package_service")
public class PackageService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int packageId;

    @Column(nullable = false)
    private String packageName;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer duration; // in days

    // Getters and setters
}

