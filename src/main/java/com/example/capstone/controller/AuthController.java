package com.example.capstone.controller;


import com.example.capstone.constant.AppConstant;
import com.example.capstone.domain.payload.request.LoginRequest;
import com.example.capstone.domain.payload.request.RegisterRequest;

import com.example.capstone.domain.payload.request.ResetPasswordRequest;
import com.example.capstone.service.AuthService;
import com.example.capstone.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;


@RestController
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterRequest req) {
        return authService.register(req);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> generateToken(@RequestBody LoginRequest req) {
        return authService.generateToken(req);
    }

    @PutMapping("/change-password")
    public ResponseEntity<Object> changePassword(@RequestBody ResetPasswordRequest req, Principal principal) {
        if (principal != null) {
            return authService.changePassword(req,principal.getName());
        } else {
            return ResponseUtil.build(AppConstant.ResponseCode.NOT_LOGGED_IN,null, HttpStatus.FORBIDDEN);
        }

    }

}
