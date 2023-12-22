package com.hacker.hacker.service;

import com.hacker.hacker.common.ApiResponse;
import com.hacker.hacker.common.response.SuccessMessage;
import com.hacker.hacker.dto.TranscriptByIdDTO;
import com.hacker.hacker.dto.TranscriptDTO;
import com.hacker.hacker.dto.TranscriptDataDTO;
import com.hacker.hacker.model.Transcript;
import com.hacker.hacker.repository.TranscriptRepository;
import com.hacker.hacker.repository.VideoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TranscriptService {
    private final VideoRepository videoRepository;
    private final TranscriptRepository transcriptRepository;
    public TranscriptService(VideoRepository videoRepository, TranscriptRepository transcriptRepository) {
        this.videoRepository = videoRepository;
        this.transcriptRepository = transcriptRepository;
    }

    public ApiResponse<TranscriptDataDTO> getTranscriptsByVideoId(Long videoId){
        TranscriptDataDTO transcriptDataDTO = videoRepository.findById(videoId)
                .map(video -> {
                    List<TranscriptDTO> transcriptDtos = video.getTranscripts().stream()
                            .sorted(Comparator.comparing(Transcript::getStart))
                            .map(t -> new TranscriptDTO(
                                    t.getTranscriptId(),
                                    t.getSentence(),
                                    t.getStart(),
                                    t.getDuration()))
                            .collect(Collectors.toList());
                    return new TranscriptDataDTO(videoId, transcriptDtos);
                })
                .orElseThrow(() -> new EntityNotFoundException("자막을 찾을 수 없습니다));"));
        return ApiResponse.success(SuccessMessage.GET_TRANSCRIPTS_SUCCESS, transcriptDataDTO);

    }

    public ApiResponse<TranscriptByIdDTO> getTranscriptByTranscriptId(Long videoId, Long transcriptId){
        Optional<Transcript> optionalTranscript = transcriptRepository.findById(transcriptId);

        if (optionalTranscript.isEmpty()) {
            throw new EntityNotFoundException("Transcript not found with id: " + transcriptId + " for video with id: " + videoId);
        }
        Transcript transcript = optionalTranscript.get();
        return ApiResponse.success(SuccessMessage.GET_TRANSCRIPTS_SUCCESS, new TranscriptByIdDTO(transcript.getVideo().getVideoId(), transcript.getTranscriptId(), transcript.getStart(), transcript.getDuration(), transcript.getSentence()));
    }

}

