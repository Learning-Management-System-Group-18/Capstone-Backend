package com.example.capstone.service;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.domain.dao.*;
import com.example.capstone.domain.dto.OrderDto;
import com.example.capstone.domain.payload.response.OrderResponse;
import com.example.capstone.repository.*;
import com.example.capstone.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrderService {
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private SlideRepository slideRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private VideoCompletedRepository videoCompletedRepository;

    @Autowired
    private SlideCompletedRepository slideCompletedRepository;

    @Autowired
    private QuizCompletedRepository quizCompletedRepository;

    @Autowired
    private ReviewRepository reviewRepository;


    public ResponseEntity<Object> createOrder(Long courseId, String email) {
        try {
            Optional<User> optionalUser = userRepository.findUserByEmail(email);
            if (optionalUser.isEmpty()) {
                log.info("User with Email [{}] not found ", email);
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND,
                        null,
                        HttpStatus.BAD_REQUEST);
            }

            Optional<Course> optionalCourse = courseRepository.findById(courseId);
            if (optionalCourse.isEmpty()) {
                log.info("Course with ID [{}] not found ", courseId);
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND,
                        null,
                        HttpStatus.BAD_REQUEST);
            }


            Order order = new Order();
            String orderId = RandomStringUtils.randomNumeric(5);
            order.setId(orderId);
            order.setUser(optionalUser.get());
            order.setCourse(optionalCourse.get());
            order.setOrderDate(LocalDateTime.now());
            orderRepository.save(order);


            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS
                    , mapper.map(order, OrderDto.class), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while trying to create order from User {} to Course with ID {}. Error : {} ",
                    email, courseId, e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getOrderByUserId(String email, String type){
        try {
            Optional<User> userOptional = userRepository.findUserByEmail(email);
            if (userOptional.isEmpty()){
                log.info("User with Email [{}] not found",email);
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND,null,HttpStatus.BAD_REQUEST);
            }

            List<Order> orderList = orderRepository.findOrderByUser(userOptional.get());
            List<OrderResponse> orderResponses = new ArrayList<>();
            for (Order order : orderList) {
                OrderResponse response = mapper.map(order, OrderResponse.class);
                Integer allVideo = videoRepository.countAllVideo(order.getCourse().getId());
                Integer allSlide = slideRepository.countAllSlide(order.getCourse().getId());
                Integer allQuiz = quizRepository.countAllQuiz(order.getCourse().getId());
                Integer completedVideo = videoCompletedRepository.countVideo(userOptional.get().getId(), order.getCourse().getId());
                Integer completedSlide = slideCompletedRepository.countSlide(userOptional.get().getId(), order.getCourse().getId());
                Integer completedQuiz = quizCompletedRepository.countQuiz(userOptional.get().getId(), order.getCourse().getId());
                response.setCountAll(allVideo + allSlide + allQuiz);
                response.setCountCompleted(completedVideo + completedSlide + completedQuiz);
                if (type == "ongoing") {
                    if (!((allVideo + allSlide + allQuiz)==(completedQuiz+completedSlide+completedVideo))){
                        orderResponses.add(response);
                    }

                } else {
                    if (((allVideo + allSlide + allQuiz)==(completedQuiz+completedSlide+completedVideo))){
                        orderResponses.add(response);
                    }
                }
            }
            log.info("Successfully retrieved Order By user ");
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, orderResponses, HttpStatus.OK);
        } catch (Exception e){
            log.error("Error occurred while trying to get order by User {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getAllOrders() {
        try {
            List<Order> orderList = orderRepository.findAll();
            List<OrderDto> orderDto = new ArrayList<>();
            for (Order order : orderList) {
                OrderDto request = mapper.map(order, OrderDto.class);
                orderDto.add(request);
            }
            log.info("Successfully retrieved All Order");
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, orderDto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while trying to get all order {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
