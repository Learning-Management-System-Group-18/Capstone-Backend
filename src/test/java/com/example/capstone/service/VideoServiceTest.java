//package com.example.capstone.service;
//
//import com.example.capstone.constant.AppConstant;
//import com.example.capstone.domain.common.ApiResponse;
//import com.example.capstone.domain.dao.Section;
//import com.example.capstone.domain.dao.Video;
//import com.example.capstone.domain.dto.SectionDto;
//import com.example.capstone.domain.dto.VideoDto;
//import com.example.capstone.repository.SectionRepository;
//import com.example.capstone.repository.VideoRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(classes = VideoService.class)
//class VideoServiceTest {
//
//    @MockBean
//    private VideoRepository videoRepository;
//
//    @MockBean
//    private SectionRepository sectionRepository;
//
//    @MockBean
//    private ModelMapper modelMapper;
//
//    @Autowired
//    private VideoService videoService;
//
//    @Test
//    void getAllVideoSuccess_Test() {
//        List<Video> videoList = new ArrayList<>();
//        Video video = Video.builder()
//                .id(1L)
//                .build();
//        videoList.add(video);
//
//        VideoDto videoDto = VideoDto.builder()
//                .id(1L)
//                .build();
//
//        //stubbing
//        when(videoRepository.findAll()).thenReturn(videoList);
//        when(modelMapper.map(any(), eq(Video.class))).thenReturn(video);
//        when(modelMapper.map(any(), eq(VideoDto.class))).thenReturn(videoDto);
//
//        ResponseEntity responseEntity = videoService.getAllVideo();
//        ApiResponse apiResponse = ((ApiResponse) responseEntity.getBody());
//
//        List<VideoDto> result = ((List<VideoDto>) apiResponse.getData());
//
//        //verification
//        assertEquals(1L, result.get(0).getId());
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//    }
//
//    @Test
//    void getAllVideoError_Test() {
//        when(videoRepository.findAll()).thenThrow(NullPointerException.class);
//        ResponseEntity<Object> responseEntity = videoService.getAllVideo();
//        ApiResponse apiResponse = ((ApiResponse) responseEntity.getBody());
//        assertEquals(AppConstant.ResponseCode.UNKNOWN_ERROR.getCode(), apiResponse.getStatus().getCode());
//    }
//
//    @Test
//    void createNewVideoSuccess_Test() {
//        Video video = Video.builder()
//                .id(1L)
//                .title("Java Basic")
//                .description("Ini Java Basic")
//                .link("http://yt.com")
//                .build();
//
//        Section section = Section.builder()
//                .id(1L)
//                .title("Introduction")
//                .build();
//
//        VideoDto videoDto = VideoDto.builder()
//                .id(1L)
//                .title("DTO Java")
//                .description("Ini Java")
//                .link("http://yt.com")
//                .section(SectionDto.builder().id(1L).build())
//                .build();
//
//        //stubbing
//        when(sectionRepository.findById(anyLong())).thenReturn(Optional.of(section));
//        when(modelMapper.map(any(), eq(Video.class))).thenReturn(video);
//        when(modelMapper.map(any(), eq(VideoDto.class))).thenReturn(videoDto);
//        when(videoRepository.save(any())).thenReturn(video);
//
//        ResponseEntity responseEntity = videoService.createNewVideo(1L, videoDto);
//        ApiResponse apiResponse = ((ApiResponse) responseEntity.getBody());
//
//        //verification
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals("DTO Java", ((VideoDto) apiResponse.getData()).getTitle());
//    }
//
//
//    @Test
//    void createNewVideoError_Test() {
//        Video video = Video.builder()
//                .id(1L)
//                .title("Java Basic")
//                .description("Ini Java Basic")
//                .link("http://yt.com")
//                .build();
//
//        Section section = Section.builder()
//                .id(1L)
//                .title("Introduction")
//                .build();
//
//        VideoDto videoDto = VideoDto.builder()
//                .id(1L)
//                .title("DTO Java")
//                .description("Ini Java")
//                .link("http://yt.com")
//                .section(SectionDto.builder().id(1L).build())
//                .build();
//
//        //stubbing
//        when(sectionRepository.findById(anyLong())).thenThrow(NullPointerException.class);
//
//        ResponseEntity responseEntity = videoService.createNewVideo(1L, videoDto);
//        ApiResponse apiResponse = ((ApiResponse) responseEntity.getBody());
//
//        //verification
//        assertEquals(AppConstant.ResponseCode.UNKNOWN_ERROR.getCode(), apiResponse.getStatus().getCode());
//
//    }
//
//
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
