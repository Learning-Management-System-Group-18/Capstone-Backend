package com.example.capstone.service;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.domain.common.ApiResponse;
import com.example.capstone.domain.dao.*;
import com.example.capstone.domain.dto.SectionDto;
import com.example.capstone.domain.dto.SlideDto;
import com.example.capstone.domain.dto.VideoDto;
import com.example.capstone.repository.*;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = VideoService.class)
class VideoServiceTest {

    @MockBean
    private SectionRepository sectionRepository;

    @MockBean
    private VideoRepository videoRepository;

    @MockBean
    private ModelMapper mapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private VideoCompletedRepository videoCompletedRepository;

    @Autowired
    private VideoService videoService;



    @Test
    void completeVideoById_Success() {
        Course course = Course.builder()
                .id(1L)
                .build();

        Section section = Section.builder()
                .id(1L)
                .course(course)
                .build();

        User user = User.builder()
                .id(1L)
                .email("ilham@gmail.com")
                .build();


        Video video = Video.builder()
                .id(1L)
                .title("Video")
                .description("description")
                .section(section)
                .link("link")
                .build();

        Boolean order = true;

        Boolean isCompleted = false;

        VideoDto videoDto = VideoDto.builder()
                .id(1L)
                .title("Video")
                .description("description")
                .completed(isCompleted)
                .link("link")
                .build();

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(videoRepository.findById(anyLong())).thenReturn(Optional.of(video));
        when(orderRepository.existsByCourseIdAndUserId(anyLong(),anyLong())).thenReturn(order);
        when(mapper.map(any(), eq(VideoDto.class))).thenReturn(videoDto);
        when(videoCompletedRepository.existsByUserIdAndVideoId(anyLong(),anyLong())).thenReturn(isCompleted);

        ResponseEntity<Object> responseEntity = videoService.completeVideo(1L,"email");
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());


    }

    @Test
    void completeVideoById_AlreadyCompleted() {
        Course course = Course.builder()
                .id(1L)
                .build();

        Section section = Section.builder()
                .id(1L)
                .course(course)
                .build();

        User user = User.builder()
                .id(1L)
                .email("ilham@gmail.com")
                .build();


        Video video = Video.builder()
                .id(1L)
                .title("Video")
                .description("description")
                .section(section)
                .link("link")
                .build();

        Boolean order = true;

        Boolean isCompleted = true;

        VideoDto videoDto = VideoDto.builder()
                .id(1L)
                .title("Video")
                .description("description")
                .link("link")
                .build();

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(videoRepository.findById(anyLong())).thenReturn(Optional.of(video));
        when(orderRepository.existsByCourseIdAndUserId(anyLong(),anyLong())).thenReturn(order);
        when(mapper.map(any(), eq(VideoDto.class))).thenReturn(videoDto);
        when(videoCompletedRepository.existsByUserIdAndVideoId(anyLong(),anyLong())).thenReturn(isCompleted);

        ResponseEntity<Object> responseEntity = videoService.completeVideo(1L,"email");
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(AppConstant.ResponseCode.ALREADY_COMPLETED.getCode(), apiResponse.getStatus().getCode());

    }

    @Test
    void completeVideoById_UserEmpty() {
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());

        ResponseEntity responseEntity = videoService.completeVideo(1L,"email");

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

    @Test
    void completeVideoById_VideoEmpty() {
        User user = User.builder()
                .id(1L)
                .email("ilham@gmail.com")
                .build();

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));

        when(videoRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity responseEntity = videoService.completeVideo(1L,"email");

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void completeVideoById_NotEnroll() {
        Course course = Course.builder()
                .id(1L)
                .build();

        Section section = Section.builder()
                .id(1L)
                .course(course)
                .build();

        User user = User.builder()
                .id(1L)
                .email("ilham@gmail.com")
                .build();


        Video video = Video.builder()
                .id(1L)
                .title("Video")
                .description("description")
                .section(section)
                .link("link")
                .build();

        Boolean order = false;



        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(videoRepository.findById(anyLong())).thenReturn(Optional.of(video));
        when(orderRepository.existsByCourseIdAndUserId(anyLong(),anyLong())).thenReturn(order);

        ResponseEntity responseEntity = videoService.completeVideo(1L,"email");

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());

    }

    @Test
    void completeVideoById_Error() {
        when(userRepository.findUserByEmail(anyString())).thenThrow(NullPointerException.class);
        ResponseEntity responseEntity = videoService.completeVideo(1L,"email");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void createNewVideo_Success() {
        Course course = Course.builder()
                .id(1L)
                .build();

        Section section = Section.builder()
                .id(1L)
                .course(course)
                .build();

        Video video = Video.builder()
                .id(1L)
                .title("Video")
                .description("description")
                .section(section)
                .link("link")
                .build();

        VideoDto videoDto = VideoDto.builder()
                .id(1L)
                .title("Video")
                .description("description")
                .link("link")
                .build();

        when(sectionRepository.findById(anyLong())).thenReturn(Optional.of(section));
        when(mapper.map(any(), eq(Video.class))).thenReturn(video);
        when(videoRepository.save(any())).thenReturn(video);
        when(mapper.map(any(), eq(VideoDto.class))).thenReturn(videoDto);

        ResponseEntity<Object> responseEntity = videoService.createNewVideo(1L, videoDto);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Video", ((VideoDto) apiResponse.getData()).getTitle());

    }

    @Test
    void createNewVideo_SectionEmpty() {
        VideoDto videoDto = VideoDto.builder()
                .id(1L)
                .title("Video")
                .description("description")
                .link("link")
                .build();

        when(sectionRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = videoService.createNewVideo(1L, videoDto);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

    @Test
    void createNewVideo_Error() {
        VideoDto videoDto = VideoDto.builder()
                .id(1L)
                .title("Video")
                .description("description")
                .link("link")
                .build();

        when(sectionRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = videoService.createNewVideo(1L, videoDto);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(AppConstant.ResponseCode.UNKNOWN_ERROR.getCode(), apiResponse.getStatus().getCode());
    }

    @Test
    void getVideoById_Success() {
        Course course = Course.builder()
                .id(1L)
                .build();

        Section section = Section.builder()
                .id(1L)
                .course(course)
                .build();

        User user = User.builder()
                .id(1L)
                .email("ilham@gmail.com")
                .build();


        Video video = Video.builder()
                .id(1L)
                .title("Video")
                .description("description")
                .section(section)
                .link("link")
                .build();

        Boolean order = true;

        Boolean isCompleted = true;

        VideoDto videoDto = VideoDto.builder()
                .id(1L)
                .title("Video")
                .description("description")
                .link("link")
                .build();



        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(videoRepository.findById(anyLong())).thenReturn(Optional.of(video));
        when(orderRepository.existsByCourseIdAndUserId(anyLong(),anyLong())).thenReturn(order);
        when(mapper.map(any(), eq(VideoDto.class))).thenReturn(videoDto);
        when(videoCompletedRepository.existsByUserIdAndVideoId(anyLong(),anyLong())).thenReturn(isCompleted);


        ResponseEntity responseEntity = videoService.getVideoById(1L,"email");
        ApiResponse apiResponse = ((ApiResponse) responseEntity.getBody());


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Video", ((VideoDto) apiResponse.getData()).getTitle());
    }

    @Test
    void getVideoById_UserEmpty() {
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());

        ResponseEntity responseEntity = videoService.getVideoById(1L,"email");

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

    @Test
    void getVideoById_VideoEmpty() {
        User user = User.builder()
                .id(1L)
                .email("ilham@gmail.com")
                .build();

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));

        when(videoRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity responseEntity = videoService.getVideoById(1L,"email");

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

    @Test
    void getVideoById_NotEnroll() {
        Course course = Course.builder()
                .id(1L)
                .build();

        Section section = Section.builder()
                .id(1L)
                .course(course)
                .build();

        User user = User.builder()
                .id(1L)
                .email("ilham@gmail.com")
                .build();


        Video video = Video.builder()
                .id(1L)
                .title("Video")
                .description("description")
                .section(section)
                .link("link")
                .build();

        Boolean order = false;


        VideoDto videoDto = VideoDto.builder()
                .id(1L)
                .title("Video")
                .description("description")
                .link("link")
                .build();


        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(videoRepository.findById(anyLong())).thenReturn(Optional.of(video));
        when(orderRepository.existsByCourseIdAndUserId(anyLong(),anyLong())).thenReturn(order);
        when(mapper.map(any(), eq(VideoDto.class))).thenReturn(videoDto);


        ResponseEntity responseEntity = videoService.getVideoById(1L,"email");
        ApiResponse apiResponse = ((ApiResponse) responseEntity.getBody());

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    void getVideoById_Error() {
        when(userRepository.findUserByEmail(anyString())).thenThrow(NullPointerException.class);
        ResponseEntity responseEntity = videoService.getVideoById(1L,"email");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

    }

    @Test
    void updateById_Success() {
        Section section = Section.builder()
                .id(1L)
                .build();

        Video video = Video.builder()
                .id(1L)
                .title("Video")
                .section(section)
                .build();

        VideoDto videoDto = VideoDto.builder()
                .id(1L)
                .title("Video")
                .build();

        when(videoRepository.findById(anyLong())).thenReturn(Optional.of(video));
        when(sectionRepository.findById(anyLong())).thenReturn(Optional.of(section));
        when(mapper.map(any(),eq(Video.class))).thenReturn(video);
        when(mapper.map(any(),eq(VideoDto.class))).thenReturn(videoDto);
        when(videoRepository.save(any())).thenReturn(video);

        ResponseEntity responseEntity = videoService.updateVideo(1L,videoDto);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Video", ((VideoDto) apiResponse.getData()).getTitle());

    }

    @Test
    void updateById_VideoEmpty() {
        VideoDto videoDto = VideoDto.builder()
                .id(1L)
                .title("Video")
                .build();

        when(videoRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity responseEntity = videoService.updateVideo(1L,videoDto);

        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        assertEquals(AppConstant.ResponseCode.DATA_NOT_FOUND.getCode(), apiResponse.getStatus().getCode());

    }

    @Test
    void updateById_Error() {
        VideoDto videoDto = VideoDto.builder()
                .id(1L)
                .title("Video")
                .build();

        when(videoRepository.findById(anyLong())).thenThrow(NullPointerException.class);

        ResponseEntity responseEntity = videoService.updateVideo(1L,videoDto);

        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        assertEquals(AppConstant.ResponseCode.UNKNOWN_ERROR.getCode(), apiResponse.getStatus().getCode());

    }

    @Test
    void deleteById_Success() {
        when(videoRepository.findById(anyLong())).thenReturn(Optional.of(Video
                .builder()
                .id(1L)
                .build()));

        doNothing().when(videoRepository).delete(any());

        ResponseEntity<Object> responseEntity = videoService.deleteById(1L);
        verify(videoRepository, times(1)).delete(any());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void deleteById_VideoEmpty() {
        when(videoRepository.findById(anyLong())).thenReturn(Optional.empty());


        ResponseEntity<Object> responseEntity = videoService.deleteById(1L);

        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        assertEquals(AppConstant.ResponseCode.DATA_NOT_FOUND.getCode(), apiResponse.getStatus().getCode());
    }

    @Test
    void deleteById_Error() {
        when(videoRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = videoService.deleteById(1L);

        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        assertEquals(AppConstant.ResponseCode.UNKNOWN_ERROR.getCode(), apiResponse.getStatus().getCode());
    }


}













