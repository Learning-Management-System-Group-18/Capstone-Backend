package com.example.capstone.service;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.domain.common.ApiResponse;
import com.example.capstone.domain.dao.*;
import com.example.capstone.domain.dto.ContentDto;
import com.example.capstone.domain.dto.CourseDto;
import com.example.capstone.domain.dto.SectionDto;
import com.example.capstone.domain.payload.response.QuizResponse;
import com.example.capstone.domain.payload.response.SlideResponse;
import com.example.capstone.domain.payload.response.VideoResponse;
import com.example.capstone.repository.*;
import org.apache.http.protocol.HTTP;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SectionService.class)
class SectionServiceTest {
    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private ModelMapper mapper;

    @MockBean
    private SectionRepository sectionRepository;

    @MockBean
    private SlideRepository slideRepository;

    @MockBean
    private VideoRepository videoRepository;

    @MockBean
    private QuizRepository quizRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private SlideCompletedRepository slideCompletedRepository;

    @MockBean
    private VideoCompletedRepository videoCompletedRepository;

    @MockBean
    private QuizCompletedRepository quizCompletedRepository;

    @Autowired
    private SectionService sectionService;

    @Test
    void getAllSectionByCourseId_Success() {
        List<Section> sections = new ArrayList<>();
        List<SectionDto> sectionDtos = new ArrayList<>();
        Section section = Section.builder()
                .id(1L)
                .build();

        SectionDto sectionDto = SectionDto.builder()
                .id(1L)
                .title("Section")
                .build();

        sections.add(section);
        sectionDtos.add(sectionDto);



        when(sectionRepository.findAllByCourseId(anyLong())).thenReturn(sections);
        when(mapper.map(any(),eq(SectionDto.class))).thenReturn(sectionDto);

        ResponseEntity<Object> responseEntity = sectionService.getAllSectionByCourseId(1L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        List<SectionDto> result = (List<SectionDto>) apiResponse.getData();


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Section", result.get(0).getTitle());

    }

    @Test
    void getAllSectionByCourseId_Error() {
        when(sectionRepository.findAllByCourseId(anyLong())).thenThrow(NullPointerException.class);
        ResponseEntity responseEntity = sectionService.getAllSectionByCourseId(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void getSectionById_Success() {
        Section section = Section.builder()
                .id(1L)
                .build();

        SectionDto sectionDto = SectionDto.builder()
                .id(1L)
                .title("Section")
                .build();

        when(sectionRepository.findById(anyLong())).thenReturn(Optional.of(section));
        when(mapper.map(any(),eq(SectionDto.class))).thenReturn(sectionDto);

        ResponseEntity<Object> responseEntity = sectionService.getSectionById(1L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Section",((SectionDto) apiResponse.getData()).getTitle());

    }

    @Test
    void getSectionById_SectionEmpty() {
        when(sectionRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity responseEntity = sectionService.getSectionById(1L);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void getSectionById_Error() {
        when(sectionRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        ResponseEntity responseEntity = sectionService.getSectionById(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void getAllContentBySectionId_Success() {
        List<Quiz> quizList = new ArrayList<>();
        List<Slide> slideList = new ArrayList<>();
        List<Video> videoList = new ArrayList<>();

        List<QuizResponse> quizResponses = new ArrayList<>();
        List<SlideResponse> slideResponses = new ArrayList<>();
        List<VideoResponse> videoResponses = new ArrayList<>();

        QuizResponse quizResponse = QuizResponse.builder()
                .id(1L)
                .title("Video")
                .description("description")
                .link("link")
                .build();

        VideoResponse videoResponse = VideoResponse.builder()
                .id(1L)
                .title("Video")
                .description("description")
                .link("link")
                .build();

        SlideResponse slideResponse = SlideResponse.builder()
                .id(1L)
                .title("Slide")
                .description("description")
                .link("link")
                .build();

        Quiz quiz = Quiz.builder()
                .id(1L)
                .title("Video")
                .description("description")
                .link("link")
                .build();

        Video video = Video.builder()
                .id(1L)
                .title("Video")
                .description("description")
                .link("link")
                .build();

        Slide slide = Slide.builder()
                .id(1L)
                .title("Slide")
                .description("description")
                .link("link")
                .build();

        User user = User.builder()
                .id(1L)
                .email("ilham@gmail.com")
                .build();

        quizList.add(quiz);
        slideList.add(slide);
        videoList.add(video);

        quizResponses.add(quizResponse);
        slideResponses.add(slideResponse);
        videoResponses.add(videoResponse);


        when(userRepository.findUserByEmail(any())).thenReturn(Optional.of(user));
        when(slideRepository.findAllBySectionId(anyLong())).thenReturn(slideList);
        when(quizRepository.findAllBySectionId(anyLong())).thenReturn(quizList);
        when(videoRepository.findAllBySectionId(anyLong())).thenReturn(videoList);
        when(mapper.map(any(),eq(VideoResponse.class))).thenReturn(videoResponse);
        when(mapper.map(any(),eq(SlideResponse.class))).thenReturn(slideResponse);
        when(mapper.map(any(),eq(QuizResponse.class))).thenReturn(quizResponse);

        ResponseEntity<Object> responseEntity = sectionService.getAllContentBySectionId(1L,"email");
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());


    }

    @Test
    void getAllContentBySectionId_UserEmpty() {
        when(userRepository.findUserByEmail(any())).thenReturn(Optional.empty());
        ResponseEntity responseEntity = sectionService.getAllContentBySectionId(1L,"email");
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

    @Test
    void getAllContentBySectionId_Error() {
        when(userRepository.findUserByEmail(anyString())).thenThrow(NullPointerException.class);
        ResponseEntity responseEntity = sectionService.getAllContentBySectionId(1L,"email");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void createNewSection_Success() {
        Course course = Course.builder()
                .id(1L)
                .build();

        Section section = Section.builder()
                .id(1L)
                .title("Section")
                .course(course)
                .build();

        CourseDto courseDto = CourseDto.builder()
                .id(1L)
                .build();

        SectionDto sectionDto = SectionDto.builder()
                .id(1L)
                .title("Section")
                .course(courseDto)
                .build();

        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
        when(mapper.map(any(), eq(Section.class))).thenReturn(section);
        when(sectionRepository.save(any())).thenReturn(section);
        when(mapper.map(any(), eq(SectionDto.class))).thenReturn(sectionDto);

        ResponseEntity<Object> responseEntity = sectionService.createNewSection(1L, sectionDto);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Section", ((SectionDto) apiResponse.getData()).getTitle());
    }

    @Test
    void createNewSection_EmptyCourse() {
        SectionDto sectionDto = SectionDto.builder()
                .id(1L)
                .title("Section")
                .build();

        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Object> responseEntity = sectionService.createNewSection(1L, sectionDto);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

    @Test
    void createNewSection_Error() {
        SectionDto sectionDto = SectionDto.builder()
                .id(1L)
                .title("Section")
                .build();

        when(courseRepository.findById(anyLong())).thenThrow(NullPointerException.class);

        ResponseEntity<Object> responseEntity = sectionService.createNewSection(1L, sectionDto);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(AppConstant.ResponseCode.UNKNOWN_ERROR.getCode(), apiResponse.getStatus().getCode());
    }

    @Test
    void deleteById_Success() {
        when(sectionRepository.findById(anyLong())).thenReturn(Optional.of(Section
                .builder()
                .id(1L)
                .title("Section")
                .build()));

        doNothing().when(sectionRepository).delete(any());

        ResponseEntity<Object> responseEntity = sectionService.deleteById(1L);
        verify(sectionRepository, times(1)).delete(any());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void deleteById_SectionEmpty() {
        when(sectionRepository.findById(anyLong())).thenReturn(Optional.empty());


        ResponseEntity<Object> responseEntity = sectionService.deleteById(1L);

        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        assertEquals(AppConstant.ResponseCode.DATA_NOT_FOUND.getCode(), apiResponse.getStatus().getCode());
    }

    @Test
    void deleteById_Error() {
        when(sectionRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = sectionService.deleteById(1L);

        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        assertEquals(AppConstant.ResponseCode.UNKNOWN_ERROR.getCode(), apiResponse.getStatus().getCode());
    }


}