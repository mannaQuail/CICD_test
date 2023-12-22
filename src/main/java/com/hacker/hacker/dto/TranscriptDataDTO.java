package com.hacker.hacker.dto;

import com.hacker.hacker.model.Transcript;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranscriptDataDTO {
    private Long videoId;
    private List<TranscriptDTO> transcripts;
}
