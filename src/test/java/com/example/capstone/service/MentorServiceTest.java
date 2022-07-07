package com.example.capstone.service;

import com.example.capstone.repository.CourseRepository;
import com.example.capstone.repository.MentorRepository;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

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

}