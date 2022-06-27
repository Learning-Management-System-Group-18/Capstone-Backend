package com.example.capstone.service;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.domain.dao.Course;
import com.example.capstone.domain.dao.Order;
import com.example.capstone.domain.dao.User;
import com.example.capstone.domain.dto.OrderDto;
import com.example.capstone.repository.CourseRepository;
import com.example.capstone.repository.OrderRepository;
import com.example.capstone.repository.ReviewRepository;
import com.example.capstone.repository.UserRepository;
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
            OrderDto request = new OrderDto();
            Order order = mapper.map(request, Order.class);
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

    public ResponseEntity<Object> getAllOrders() {
        try {
            List<Order> orderList = orderRepository.findAll();
            List<OrderDto> orderDto = new ArrayList<>();
            for (Order order : orderList) {
                OrderDto request = mapper.map(order, OrderDto.class);
                orderDto.add(request);
            }

            log.info("Successfully retrieved All Order");
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS,
                    orderDto,
                    HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while trying to get all order {}",e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
