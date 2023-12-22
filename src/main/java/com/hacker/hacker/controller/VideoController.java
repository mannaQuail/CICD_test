package com.hacker.hacker.controller;

import com.hacker.hacker.common.ApiResponse;
import com.hacker.hacker.dto.VideoDTO;
import com.hacker.hacker.dto.VideoListDTO;
import com.hacker.hacker.dto.VideoUploadDTO;
import com.hacker.hacker.dto.VideosByCategoryDTO;
import com.hacker.hacker.model.CategoryVideo;
import com.hacker.hacker.service.VideoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VideoController {
    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping("/videos/categories")
    public ApiResponse<List<VideosByCategoryDTO>> getVideosByCategoryIds(@RequestParam List<Long> categoryId){
        return videoService.getVideosByCategoryIds(categoryId);

    }

    @GetMapping("/videos")
    public ApiResponse<List<VideoListDTO>> getAllVideos(){
        return videoService.getAllVideos();
    }

    @GetMapping("/videos/{videoId}")
    public ApiResponse<VideoDTO> getVideosById(@RequestParam int videoId) { return videoService.getVideosById(videoId); }

    @PostMapping("/videos")
    public ApiResponse<VideoUploadDTO> uploadVideo(@RequestParam String link) throws Exception {
        return videoService.fetchDataFromFlask(link);
    }
}
