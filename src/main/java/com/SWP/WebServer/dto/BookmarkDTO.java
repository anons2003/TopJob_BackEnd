package com.SWP.WebServer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkDTO {
    private Long id;
    private Long jobId;
    private int jobSeekerId;
    private byte isBookmarked;

}
