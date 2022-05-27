package com.example.capstone.controller;

import com.example.capstone.domain.payload.EmailPassword;
import com.example.capstone.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody EmailPassword emailPassword, Principal principal) {
        if(principal!= null){
            return authService.register(emailPassword, principal.getName());
        }
        return authService.register(emailPassword, null);
    }

    @PostMapping("/login")
    @SecurityRequirements
    public ResponseEntity<Object> generateToken(@RequestBody EmailPassword req) {
        return authService.generateToken(req);
    }
}
