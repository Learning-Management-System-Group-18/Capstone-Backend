package com.example.capstone.controller;


import com.example.capstone.domain.payload.request.LoginRequest;
import com.example.capstone.domain.payload.request.RegisterRequest;

import com.example.capstone.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


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

}
