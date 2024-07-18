package com.SWP.WebServer.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlogDTO {
    private Long id;
    private String title;
    private String content;
    private String author;
    private String imageUrl;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
