package com.example.capstone.service;

import com.example.capstone.domain.common.ApiResponse;
import com.example.capstone.domain.dao.Course;
import com.example.capstone.domain.dao.Mentor;
import com.example.capstone.domain.dao.Tool;
import com.example.capstone.domain.dto.CourseDto;
import com.example.capstone.domain.dto.MentorDto;
import com.example.capstone.domain.dto.ToolDto;
import com.example.capstone.repository.CourseRepository;
import com.example.capstone.repository.MentorRepository;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MentorService.class)
class MentorServiceTest {

    @MockBean
    private  UploadFileService uploadFileService;

    @MockBean
    private MentorRepository mentorRepository;

    @MockBean
    private ModelMapper mapper;

    @MockBean
    private CourseRepository courseRepository;

    @Autowired
    private MentorService mentorService;

    @Test
    void getAllMentorByCourseId_Success() {
        List<Mentor> mentors = new ArrayList<>();
        List<MentorDto> mentorDtos = new ArrayList<>();
        Mentor mentor = Mentor.builder()
                .id(1L)
                .name("mentor")
                .build();

        MentorDto mentorDto = MentorDto.builder()
                .id(1L)
                .name("mentor")
                .build();

        mentors.add(mentor);
        mentorDtos.add(mentorDto);


        when(mentorRepository.findAllByCourseId(anyLong())).thenReturn(mentors);
        when(mapper.map(any(),eq(MentorDto.class))).thenReturn(mentorDto);

        ResponseEntity<Object> responseEntity = mentorService.getAllMentorByCourseId(1L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        List<MentorDto> result = (List<MentorDto>) apiResponse.getData();


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("mentor", result.get(0).getName());
    }

    @Test
    void getAllMentorByCourseId_Error() {
        when(mentorRepository.findAllByCourseId(anyLong())).thenThrow(NullPointerException.class);
        ResponseEntity responseEntity = mentorService.getAllMentorByCourseId(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

    }

    @Test
    void topMentor_Success() {
        List<Mentor> mentors = new ArrayList<>();
        Mentor mentor = Mentor.builder()
                .id(1L)
                .name("mentor")
                .build();

        MentorDto mentorDto = MentorDto.builder()
                .id(1L)
                .name("mentor")
                .build();

        mentors.add(mentor);


        when(mentorRepository.findTop10ByOrderById()).thenReturn(mentors);
        when(mapper.map(any(),eq(MentorDto.class))).thenReturn(mentorDto);

        ResponseEntity<Object> responseEntity = mentorService.topMentor();
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        List<MentorDto> result = (List<MentorDto>) apiResponse.getData();


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("mentor", result.get(0).getName());

    }

    @Test
    void topMentor_Error() {
        when(mentorRepository.findTop10ByOrderById()).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = mentorService.topMentor();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void newMentor_Success() {

        Course course = Course.builder()
                .id(1L)
                .title("title")
                .build();

        CourseDto courseDto = CourseDto.builder()
                .id(1L)
                .title("title")
                .build();

        Mentor mentor = Mentor.builder()
                .id(1L)
                .name("mentor")
                .course(course)
                .build();

        MentorDto mentorDto = MentorDto.builder()
                .id(1L)
                .name("mentor")
                .course(courseDto)
                .build();

        MockMultipartFile file = new MockMultipartFile(
                "name",
                "some-name",
                MediaType.TEXT_PLAIN_VALUE,
                "think of this as image".getBytes());



        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
        when(mapper.map(any(), eq(Mentor.class))).thenReturn(mentor);
        when(mentorRepository.save(any())).thenReturn(mentor);
        when(mapper.map(any(), eq(MentorDto.class))).thenReturn(mentorDto);

        ResponseEntity<Object> responseEntity = mentorService.createNewMentor(1L,mentorDto,file);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("mentor", ((MentorDto) apiResponse.getData()).getName());

        verify(uploadFileService, times(1)).upload(any(),any(),any(),any());

    }

    @Test
    void newMentor_Error() {
        MentorDto mentorDto = MentorDto.builder()
                .id(1L)
                .name("mentor")
                .build();

        MockMultipartFile file = new MockMultipartFile(
                "name",
                "some-name",
                MediaType.TEXT_PLAIN_VALUE,
                "think of this as image".getBytes());

        when(courseRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = mentorService.createNewMentor(1L,mentorDto,file);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }


    @Test
    void updateById_Success() {
        Course course = Course.builder()
                .id(1L)
                .title("title")
                .build();

        CourseDto courseDto = CourseDto.builder()
                .id(1L)
                .title("title")
                .build();

        Mentor mentor = Mentor.builder()
                .id(1L)
                .name("mentor")
                .course(course)
                .build();

        MentorDto mentorDto = MentorDto.builder()
                .id(1L)
                .name("mentor")
                .course(courseDto)
                .build();

        MockMultipartFile file = new MockMultipartFile(
                "name",
                "some-name",
                MediaType.TEXT_PLAIN_VALUE,
                "think of this as image".getBytes());

        when(mentorRepository.findById(anyLong())).thenReturn(Optional.of(mentor));
        when(mentorRepository.save(any())).thenReturn(mentor);
        when(mapper.map(any(), eq(MentorDto.class))).thenReturn(mentorDto);

        ResponseEntity<Object> responseEntity = mentorService.updateById(1L,mentorDto,file);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("mentor", ((MentorDto) apiResponse.getData()).getName());

        verify(uploadFileService,times(1)).delete(any(),any());
        verify(uploadFileService,times(1)).upload(any(),any(),any(),any());
    }

    @Test
    void updateById_NotFound() {
        MentorDto mentorDto = MentorDto.builder()
                .id(1L)
                .name("mentor")
                .build();

        MockMultipartFile file = new MockMultipartFile(
                "name",
                "some-name",
                MediaType.TEXT_PLAIN_VALUE,
                "think of this as image".getBytes());

        when(mentorRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = mentorService.updateById(1L,mentorDto,file);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

    @Test
    void updateById_ImageEmpty_Success() {
        Course course = Course.builder()
                .id(1L)
                .title("title")
                .build();

        CourseDto courseDto = CourseDto.builder()
                .id(1L)
                .title("title")
                .build();

        Mentor mentor = Mentor.builder()
                .id(1L)
                .name("mentor")
                .course(course)
                .build();

        MentorDto mentorDto = MentorDto.builder()
                .id(1L)
                .name("mentor")
                .course(courseDto)
                .build();


        when(mentorRepository.findById(anyLong())).thenReturn(Optional.of(mentor));
        when(mentorRepository.save(any())).thenReturn(mentor);
        when(mapper.map(any(), eq(MentorDto.class))).thenReturn(mentorDto);

        ResponseEntity<Object> responseEntity = mentorService.updateById(1L,mentorDto,null);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("mentor", ((MentorDto) apiResponse.getData()).getName());




    }

    @Test
    void updateById_Error() {
        MentorDto mentorDto = MentorDto.builder()
                .id(1L)
                .name("mentor")
                .build();

        MockMultipartFile file = new MockMultipartFile(
                "name",
                "some-name",
                MediaType.TEXT_PLAIN_VALUE,
                "think of this as image".getBytes());

        when(mentorRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = mentorService.updateById(1L,mentorDto,file);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

    }



}