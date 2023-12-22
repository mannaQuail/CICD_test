package com.hacker.hacker.controller;

import com.hacker.hacker.common.ApiResponse;
import com.hacker.hacker.dto.TranscriptByIdDTO;
import com.hacker.hacker.dto.TranscriptDataDTO;
import com.hacker.hacker.service.TranscriptService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TranscriptController {
    private final TranscriptService transcriptService;

    public TranscriptController(TranscriptService transcriptService) {
        this.transcriptService = transcriptService;
    }

    @GetMapping("/videos/{videoId}/transcripts")
    public ApiResponse<TranscriptDataDTO>getTranscriptsByVideoId(@PathVariable Long videoId){
        return transcriptService.getTranscriptsByVideoId(videoId);
    }

    @GetMapping("/videos/{videoId}/transcripts/{transcriptId}")
    public ApiResponse<TranscriptByIdDTO>getTranscriptById(@PathVariable Long videoId, @PathVariable Long transcriptId){
        return transcriptService.getTranscriptByTranscriptId(videoId, transcriptId);
    }
}
