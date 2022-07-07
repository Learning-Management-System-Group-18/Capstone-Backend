package com.example.capstone.service;

import com.example.capstone.repository.CourseRepository;
import com.example.capstone.repository.ToolRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ToolService.class)
class ToolServiceTest {
    @MockBean
    private  UploadFileService uploadFileService;

    @MockBean
    private ToolRepository toolRepository;

    @MockBean
    private ModelMapper mapper;

    @MockBean
    private CourseRepository courseRepository;

    @Autowired
    private ToolService toolService;

}