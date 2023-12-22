package com.hacker.hacker.service;

import com.hacker.hacker.common.ApiResponse;
import com.hacker.hacker.common.response.SuccessMessage;
import com.hacker.hacker.dto.TranscriptByIdDTO;
import com.hacker.hacker.model.Transcript;
import com.hacker.hacker.model.Video;
import com.hacker.hacker.repository.TranscriptRepository;
import com.hacker.hacker.repository.VideoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TranscriptServiceTest {

    @Mock
    TranscriptRepository transcriptRepository;
    @InjectMocks
    TranscriptService transcriptService;

    @Test
    void testGetTranscriptByTranscriptId() {
        //Given
        Transcript transcript = new Transcript();
        Video video = new Video();
        video.setVideoId(1L);
        Long transcriptId = 1L;
        transcript.setTranscriptId(transcriptId);
        transcript.setStart(0.1);
        transcript.setDuration(0.2);
        transcript.setSentence("asdf");
        transcript.setVideo(video);
        /**
         * findById method call 시 stubbing
         */
        when(transcriptRepository.findById(1L)).thenReturn(Optional.of(transcript));

        //When
        /**
         * 실제 service layer function call
         */
        ApiResponse<TranscriptByIdDTO> response = transcriptService.getTranscriptByTranscriptId(1L,transcriptId);

        //Then
        assertNotNull(response);
        assertEquals(1L, response.getData().getTranscriptId());
        assertEquals(0.1, response.getData().getStart());
        assertEquals(0.2, response.getData().getDuration());
        assertEquals("asdf", response.getData().getSentence());
        assertEquals(SuccessMessage.GET_TRANSCRIPTS_SUCCESS.getMessage(),response.getMessage());
        assertEquals(1L, response.getData().getVideoId());
    }
}