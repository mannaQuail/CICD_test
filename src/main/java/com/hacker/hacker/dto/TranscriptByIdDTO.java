package com.hacker.hacker.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranscriptByIdDTO {
    private Long videoId;
    private Long transcriptId;
    private Double start;
    private Double duration;
    private String sentence;
}
