package com.hacker.hacker.service;

import com.hacker.hacker.common.ApiResponse;
import com.hacker.hacker.common.response.SuccessMessage;
import com.hacker.hacker.dto.VideoDTO;
import com.hacker.hacker.dto.VideoListDTO;
import com.hacker.hacker.dto.VideosByCategoryDTO;
import com.hacker.hacker.model.CategoryVideo;
import com.hacker.hacker.model.Video;
import com.hacker.hacker.repository.CategoryVideoRepository;
import com.hacker.hacker.repository.VideoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VideoServiceTest {

    @Mock
    private VideoRepository videoRepository;
    @Mock
    private CategoryVideoRepository categoryVideoRepository;
    @InjectMocks
    private VideoService videoService;

    @Test
    @DisplayName("전체 영상 가져오기 테스트")
    void getAllVideosTest() {
        // Given
        Video video1 = new Video();
        video1.setVideoId(1L);
        video1.setVideoTitle("Video 1");
        Video video2 = new Video();
        video2.setVideoId(2L);
        video2.setVideoTitle("Video 2");
        /**
         * 아래의 save 메소드는 아무런 동작을 하지 않는다.
         */
        videoRepository.save(video1);
        videoRepository.save(video2);
        /**
         * findAll() 메소드가 실행되었을 때 어떤 식으로 return 해야 하는지 정해준다.
         */
        when(videoRepository.findAll()).thenReturn(Arrays.asList(video1, video2));
        /**
         * @Mock 된 repository 는 보통의 repository 처럼 동작하지 않기 때문에 .save(entity), .findAll() 등의 메소드가 작동하지 않는다
         * 대신 when() 구문으로 stubbing(Mock repository 한테 어떤 메소드를 실행했을 때 어떻게 return 해라~ 를 알려주는 느낌) 이라는 것을 해줘야 한다.
         * 아래의 videoRepository.count() 메소드는 0을 출력하게 된다.
         * count() 메소드를 실행했을 때 어떻게 return 을 해야 하는지 stubbing 을 해주지 않았기 때문이다.
         */
        System.out.println(videoRepository.count());
        /**
         * count() 메소드를 stubbing 해준다.
         */
        when(videoRepository.count()).thenReturn(2L);
        /**
         * 그럼 비로소 count() 가 2를 출력한다.
         */
        System.out.println(videoRepository.count());
        // When
        /**
         * service layer 에서 getAllVideos() 메소드를 사용하면 service layer 에 있는 videoRepository 를 사용한다.
         * Test class 에서 생성한 Mock repository 와 다른 repository 이다.
         * Mock Repository 랑 실제 Repository 랑 동작이 같은지 확인하는 테스트이기 때문이다.
         */
        ApiResponse<List<VideoListDTO>> response = videoService.getAllVideos();

        // Then
        assertNotNull(response);
        assertEquals(response.getStatus(), 200);
        assertEquals(SuccessMessage.GET_ALL_VIDEOS_SUCCESS.getMessage(), response.getMessage());
        assertEquals(2, response.getData().size());
        assertEquals("Video 1", response.getData().get(0).getVideoTitle());
        assertEquals(1L, response.getData().get(0).getVideoId());
        assertEquals("Video 2", response.getData().get(1).getVideoTitle());
        assertEquals(2L, response.getData().get(1).getVideoId());
    }

    @Test
    void getVideosByCategoryIdsTest() {
        // Given
        Long categoryId = 1L;
        Video video = new Video();
        video.setVideoId(1L);
        video.setVideoTitle("asdf");
        video.setLink("http://example.com/video");

        CategoryVideo categoryVideo = new CategoryVideo();
        categoryVideo.setVideo(video);

        when(categoryVideoRepository.findByCategory_CategoryId(categoryId))
                .thenReturn(List.of(categoryVideo));

        // When
        ApiResponse<List<VideosByCategoryDTO>> response = videoService.getVideosByCategoryIds(Arrays.asList(categoryId));

        // Then
        assertNotNull(response);
        assertEquals(SuccessMessage.VIDEOS_BY_CATEGORY_GET_SUCCESS.getMessage(), response.getMessage());
        assertEquals(1, response.getData().size());

        VideosByCategoryDTO videosByCategoryDTO = response.getData().get(0);
        assertNotNull(videosByCategoryDTO);
        assertEquals(categoryId, videosByCategoryDTO.getCategoryId());

        Set<VideoDTO> videoDTOs = videosByCategoryDTO.getVideos();
        assertNotNull(videoDTOs);
        assertEquals(1, videoDTOs.size());

        VideoDTO videoDTO = videoDTOs.iterator().next();
        assertNotNull(videoDTO);
        assertEquals(video.getVideoId(), videoDTO.getVideoId());
        assertEquals(video.getLink(), videoDTO.getVideoUrl());
    }
}