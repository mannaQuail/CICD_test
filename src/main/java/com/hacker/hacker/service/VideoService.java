package com.hacker.hacker.service;

import com.hacker.hacker.common.ApiResponse;
import com.hacker.hacker.common.response.SuccessMessage;
import com.hacker.hacker.dto.VideoDTO;
import com.hacker.hacker.dto.VideoListDTO;
import com.hacker.hacker.dto.VideoUploadDTO;
import com.hacker.hacker.dto.VideosByCategoryDTO;
import com.hacker.hacker.model.CategoryVideo;
import com.hacker.hacker.model.Transcript;
import com.hacker.hacker.model.Video;
import com.hacker.hacker.repository.CategoryVideoRepository;
import com.hacker.hacker.repository.TranscriptRepository;
import com.hacker.hacker.repository.VideoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
public class VideoService {
    private final CategoryVideoRepository categoryVideoRepository;
    private final VideoRepository videoRepository;
    private final WebClient webClient;

    private final TranscriptRepository transcriptRepository;
    ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(100 * 1024 * 1024))
            .build();
    public VideoService(CategoryVideoRepository categoryVideoRepository, VideoRepository videoRepository, TranscriptRepository transcriptRepository) {
        this.categoryVideoRepository = categoryVideoRepository;
        this.transcriptRepository = transcriptRepository;
        this.webClient = WebClient.builder()
                .exchangeStrategies(strategies)
                .build();
        this.videoRepository = videoRepository;
    }

    public ApiResponse<List<VideosByCategoryDTO>> getVideosByCategoryIds(List<Long> categoryIds){
        List<VideosByCategoryDTO> videosByCategoryDTOList = new ArrayList<>();
        for(Long categoryId : categoryIds){
            VideosByCategoryDTO videosByCategoryDTO = new VideosByCategoryDTO();
            videosByCategoryDTO.setCategoryId(categoryId);
            Set<VideoDTO> videoDTOS = new HashSet<>();
            List<CategoryVideo> categoryVideos = categoryVideoRepository.findByCategory_CategoryId(categoryId);
            for(CategoryVideo categoryVideo : categoryVideos){
                VideoDTO videoDTO = new VideoDTO();
                videoDTO.setVideoId(categoryVideo.getVideo().getVideoId());
                videoDTO.setVideoUrl(categoryVideo.getVideo().getLink());
                videoDTOS.add(videoDTO);
            }
            videosByCategoryDTO.setVideos(videoDTOS);
            videosByCategoryDTOList.add(videosByCategoryDTO);
        }
        return ApiResponse.success(SuccessMessage.VIDEOS_BY_CATEGORY_GET_SUCCESS, videosByCategoryDTOList);
    }

    public ApiResponse<VideoDTO> getVideosById(int videoId){
        VideoDTO videoDTO = new VideoDTO();
        Video video = videoRepository.findByVideoId(videoId);
        videoDTO.setVideoId(video.getVideoId());
        videoDTO.setVideoTitle(video.getVideoTitle());
        videoDTO.setVideoUrl(video.getLink());
        return ApiResponse.success(SuccessMessage.GET_VIDEOS_BY_ID_SUCCESS,videoDTO);
    }

    public ApiResponse<List<VideoListDTO>> getAllVideos(){
        List<Video> videoList = videoRepository.findAll();
        List<VideoListDTO> videoListDTOS = new ArrayList<>();
        for(Video video : videoList){
            VideoListDTO videoListDTO = new VideoListDTO();
            videoListDTO.setVideoTitle(video.getVideoTitle());
            videoListDTO.setVideoId(video.getVideoId());
            videoListDTOS.add(videoListDTO);
        }
        return ApiResponse.success(SuccessMessage.GET_ALL_VIDEOS_SUCCESS,videoListDTOS);
    }

    public ApiResponse<VideoUploadDTO> fetchDataFromFlask(String link) throws Exception {
        Map youtubeData;
        try {
            youtubeData = webClient.get()
                    .uri("http://localhost:8000/getYoutubeData/" + link)
                    .retrieve()
                    .bodyToMono(Map.class) //flask 에서 준 json 을 map 으로 변환
                    .block();
        } catch (Exception e) {
            throw new Exception("An error occurred while retrieving the data", e);
        }
        Video video = createVideo(link, youtubeData);
        VideoUploadDTO videoUploadDTO = new VideoUploadDTO();
        videoUploadDTO.setVideoId(video.getVideoId());
        videoUploadDTO.setVideoTitle(video.getVideoTitle());
        videoUploadDTO.setLink(video.getLink());
        return ApiResponse.success(SuccessMessage.VIDEO_UPLOAD_SUCCESS, videoUploadDTO);
    }

    private Video createVideo(String link, Map youtubeData) {
        Video video = new Video();
        video.setLink(link);
        video.setViews(0L);
        video.setIsDefault(true);
        video.setCreatedAt(new Date());
        videoRepository.save(video); //video에 관한 다른 로직 발생시키기 전에 repository(database)에 save 해줘야 충돌 안남
        if (youtubeData != null && !youtubeData.isEmpty()) {
            video.setVideoTitle((String) youtubeData.get("title")); //Map<String, Object> 로 되어있기 때문에 type casting 해줘야됨
            video.setCreator((String) youtubeData.get("creator"));
            Object durationObject = youtubeData.get("duration");
            if (durationObject != null) { //Long, Integer 등의 경우에 이렇게 타입 변환해주는게 통상적임
                String durationString = durationObject.toString();
                Long durationInSeconds = Long.parseLong(durationString);
                video.setDuration(durationInSeconds);
            }
            Object youtubeViews = youtubeData.get("viewCount");
            if (youtubeViews != null) {
                String youtubeViewsString = youtubeViews.toString();
                Long youtubeViewsLong = Long.parseLong(youtubeViewsString);
                video.setYoutubeViews(youtubeViewsLong);
            }
        }
        List<Map<String, Object>> transcripts = (List<Map<String, Object>>) youtubeData.get("transcripts");
        //transcript list 는 Map<String, Object> 안에 또 다른 List<Map<String, Object>> 로 담겨왔음. flask 참고
        if (transcripts != null) {
            for (Map<String, Object> transcriptMap : transcripts) {
                video.getTranscripts().add(createTranscript(transcriptMap, video));
            }
        }
        return videoRepository.save(video);
    }

    public Transcript createTranscript(Map<String, Object> transcriptMap, Video video) {
        Transcript transcript = new Transcript();
        transcript.setSentence((String) transcriptMap.get("text"));
        transcript.setStart(((Number) transcriptMap.get("start")).doubleValue()); //여기서도 double 형식의 type casting
        transcript.setDuration(((Number) transcriptMap.get("duration")).doubleValue());
        transcript.setVideo(video); // 이전에 videorepository.save(video) 했던 이유임. 이전에 save 해놨어야만 지금 setVideo를 할 수 있음
        transcript = transcriptRepository.save(transcript);
        return transcript;
    }

}
