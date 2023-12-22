package com.hacker.hacker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VideoUploadDTO {
    private Long videoId;
    private String link;
    private String videoTitle;
}
