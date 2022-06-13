package com.example.capstone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.capstone.domain.dto.SlideDto;
import com.example.capstone.service.SlideService;

@RestController
@RequestMapping("/")
public class SlideController {
    @Autowired
    private SlideService slideService;

    @GetMapping("/slide")
    public ResponseEntity<Object> getAllSlide(){
        return slideService.getAllSlide();
    }

    @PostMapping("/admin/slide")
    public ResponseEntity<Object> createNewSlide(@RequestParam(value = "sectionId")Long sectionId,
                                                    @RequestBody SlideDto request){
        return slideService.createNewSlide(sectionId,request);
    }
}
