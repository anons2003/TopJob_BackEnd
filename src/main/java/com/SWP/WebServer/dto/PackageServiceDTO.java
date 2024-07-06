package com.SWP.WebServer.dto;
import lombok.Data;

@Data
public class PackageServiceDTO {

    private Long packageId;

    private String packageName;

    private String description;

    private Double price;

    private Integer duration; // in days

    // Constructors
    public PackageServiceDTO() {
    }

    public PackageServiceDTO(Long packageId, String packageName, String description, Double price, Integer duration) {
        this.packageId = packageId;
        this.packageName = packageName;
        this.description = description;
        this.price = price;
        this.duration = duration;
    }

    // Getters and setters
    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
