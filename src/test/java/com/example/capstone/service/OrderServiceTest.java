package com.example.capstone.service;

import com.example.capstone.domain.common.ApiResponse;
import com.example.capstone.domain.dao.Course;
import com.example.capstone.domain.dao.Order;
import com.example.capstone.domain.dao.User;
import com.example.capstone.domain.dto.CourseDto;
import com.example.capstone.domain.dto.MentorDto;
import com.example.capstone.domain.dto.OrderDto;
import com.example.capstone.domain.payload.response.OrderResponse;
import com.example.capstone.domain.payload.response.RegisterResponse;
import com.example.capstone.repository.*;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Test
    void createOrder_Success() {
        User user = User.builder()
                .id(1L)
                .email("ilham@gmail.com")
                .build();

        RegisterResponse response = new RegisterResponse();
        response.setEmail("ilham@gmail.com");
        response.setFullName("ilham");

        Course course = Course.builder()
                .id(1L)
                .title("course")
                .build();

        CourseDto courseDto = CourseDto.builder()
                .id(1L)
                .title("course")
                .build();

        Order order = Order.builder()
                .id("111")
                .course(course)
                .user(user)
                .build();

        OrderDto orderDto = OrderDto.builder()
                .id("111")
                .course(courseDto)
                .user(response)
                .build();

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
        when(orderRepository.save(any())).thenReturn(order);
        when(mapper.map(any(),eq(OrderDto.class))).thenReturn(orderDto);

        ResponseEntity<Object> responseEntity = orderService.createOrder(1L,"email");
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("111",((OrderDto) apiResponse.getData()).getId());
    }

    @Test
    void createOrder_EmailEmpty() {
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = orderService.createOrder(1L,"email");
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void createOrder_CourseEmpty() {
        User user = User.builder()
                .id(1L)
                .email("ilham@gmail.com")
                .build();

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = orderService.createOrder(1L,"email");
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

    @Test
    void createOrder_Error() {
        when(userRepository.findUserByEmail(anyString())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = orderService.createOrder(1L,"email");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void getOrderByUserId_Success() {
        List<Order> orderList = new ArrayList<>();

        User user = User.builder()
                .id(1L)
                .email("ilham@gmail.com")
                .build();

        Course course = Course.builder()
                .id(1L)
                .title("course")
                .build();

        CourseDto courseDto = CourseDto.builder()
                .id(1L)
                .title("course")
                .build();

        Order order = Order.builder()
                .id("111")
                .course(course)
                .user(user)
                .build();

        Integer allVideo = 4;
        Integer allSlide = 3;
        Integer allQuiz =  3;

        Integer completedVideo = 2;
        Integer completedSlide = 2;
        Integer completedQuiz = 1;

        OrderResponse response = new OrderResponse();
        response.setCountCompleted(completedVideo + completedSlide + completedQuiz);
        response.setCountAll(allVideo + allQuiz + allSlide);
        response.setCourse(courseDto);

        orderList.add(order);


        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(orderRepository.findOrderByUser(any())).thenReturn(orderList);
        when(mapper.map(any(), eq(OrderResponse.class))).thenReturn(response);
        when(videoRepository.countAllVideo(any())).thenReturn(allVideo);
        when(slideRepository.countAllSlide(any())).thenReturn(allSlide);
        when(quizRepository.countAllQuiz(any())).thenReturn(allQuiz);
        when(videoCompletedRepository.countVideo(any(),any())).thenReturn(completedVideo);
        when(slideCompletedRepository.countSlide(any(),any())).thenReturn(completedSlide);
        when(quizCompletedRepository.countQuiz(any(),any())).thenReturn(completedQuiz);

        ResponseEntity<Object> responseEntity = orderService.getOrderByUserId("email","ongoing");
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }

    @Test
    void getOrderByUserId_EmailEmpty() {
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = orderService.getOrderByUserId("email","ongoing");
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void getOrderByUserId_Error() {
        when(userRepository.findUserByEmail(anyString())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = orderService.getOrderByUserId("email","ongoing");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void getAllOrders_Success() {
        List<Order> orderList = new ArrayList<>();
        List<OrderDto> orderDtos = new ArrayList<>();

        Order order = Order.builder()
                .id("111")
                .build();

        OrderDto orderDto = OrderDto.builder()
                .id("111")
                .build();


        orderList.add(order);
        orderDtos.add(orderDto);

        when(orderRepository.findAll()).thenReturn(orderList);
        when(mapper.map(any(), eq(OrderDto.class))).thenReturn(orderDto);

        ResponseEntity<Object> responseEntity = orderService.getAllOrders();
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        List<OrderDto> result = (List<OrderDto>) apiResponse.getData();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("111", result.get(0).getId());

    }

    @Test
    void getAllOrders_Error() {
        when(orderRepository.findAll()).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = orderService.getAllOrders();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
}