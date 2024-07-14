package com.SWP.WebServer.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "blog")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String author;
    private String imageUrl;
    private Boolean isActive;
    private LocalDateTime createdAt;

    // Getters and Setters

    public void setActive(Boolean active) {
        isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }
}
