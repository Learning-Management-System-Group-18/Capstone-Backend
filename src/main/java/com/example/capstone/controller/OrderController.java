package com.example.capstone.controller;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.service.OrderService;
import com.example.capstone.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/")
@CrossOrigin
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/auth/order")
    public ResponseEntity<Object> newOrder(@RequestParam("courseId") Long courseId,
                                           Principal principal) {
        if (principal != null){
            return orderService.createOrder(courseId, principal.getName());
        } else {
            return ResponseUtil.build(AppConstant.ResponseCode.NOT_LOGGED_IN,null, HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/auth/order/ongoing")
    public ResponseEntity<Object> ongoingCourse(Principal principal) {
        if (principal != null){
            return orderService.getOrderByUserId(principal.getName(),"ongoing");
        } else {
            return ResponseUtil.build(AppConstant.ResponseCode.NOT_LOGGED_IN,null, HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/auth/order/completed")
    public ResponseEntity<Object> completedCourse(Principal principal) {
        if (principal != null){
            return orderService.getOrderByUserId(principal.getName(), "completed");
        } else {
            return ResponseUtil.build(AppConstant.ResponseCode.NOT_LOGGED_IN,null, HttpStatus.FORBIDDEN);
        }

    }

    @GetMapping("/orders")
    public ResponseEntity<Object> getAllOrder(){
        return orderService.getAllOrders();
    }
}
