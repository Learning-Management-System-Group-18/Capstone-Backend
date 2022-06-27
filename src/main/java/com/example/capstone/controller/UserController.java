package com.example.capstone.controller;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.domain.dto.UserProfileDto;
import com.example.capstone.service.UserProfileService;
import com.example.capstone.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/")
@CrossOrigin
public class UserController {

    @Autowired
    private UserProfileService userProfileService;
    //get user profile
    @GetMapping("/profile")
    public ResponseEntity<Object> getProfile(Principal principal) {
        if (principal != null) {
            return userProfileService.getProfileByEmail(principal.getName());
        } else {
            return ResponseUtil.build(AppConstant.ResponseCode.NOT_LOGGED_IN, null, HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<Object> updateProfile(@RequestBody UserProfileDto request,
                                                Principal principal) {
        if (principal != null) {
            return userProfileService.updateProfile(request,principal.getName());
        } else {
            return ResponseUtil.build(AppConstant.ResponseCode.NOT_LOGGED_IN, null, HttpStatus.FORBIDDEN);
        }
    }


}
