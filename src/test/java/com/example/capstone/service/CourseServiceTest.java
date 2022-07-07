package com.example.capstone.service;

import com.example.capstone.repository.CategoryRepository;
import com.example.capstone.repository.CourseRepository;
import com.example.capstone.repository.OrderRepository;
import com.example.capstone.repository.ReviewRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CourseService.class)
class CourseServiceTest {

    @MockBean
    private UploadFileService uploadFileService;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private ModelMapper mapper;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private ReviewRepository reviewRepository;

    @MockBean
    private OrderRepository orderRepository;

    @Autowired
    private CourseService courseService;

}