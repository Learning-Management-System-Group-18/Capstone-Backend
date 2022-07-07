package com.example.capstone.service;

import com.example.capstone.repository.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OrderService.class)
class OrderServiceTest {

    @MockBean
    private ModelMapper mapper;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private VideoRepository videoRepository;

    @MockBean
    private SlideRepository slideRepository;

    @MockBean
    private QuizRepository quizRepository;

    @MockBean
    private VideoCompletedRepository videoCompletedRepository;

    @MockBean
    private SlideCompletedRepository slideCompletedRepository;

    @MockBean
    private QuizCompletedRepository quizCompletedRepository;

    @MockBean
    private ReviewRepository reviewRepository;

    @Autowired
    private OrderService orderService;

}