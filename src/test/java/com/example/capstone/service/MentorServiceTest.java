package com.example.capstone.service;

import com.example.capstone.domain.common.ApiResponse;
import com.example.capstone.domain.dao.Mentor;
import com.example.capstone.domain.dao.Tool;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

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

}