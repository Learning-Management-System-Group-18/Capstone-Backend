package com.example.capstone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.capstone.domain.dto.SectionDto;
import com.example.capstone.service.SectionService;

@RestController
@RequestMapping("/")
public class SectionController {
    @Autowired
    private SectionService sectionService;

    @GetMapping("/section")
    public ResponseEntity<Object> getAllSection(){
        return sectionService.getAllSection();
    }

    @PostMapping("/admin/section")
    public ResponseEntity<Object> createNewSection(@RequestParam(value = "courseId")Long courseId,
                                                    @RequestBody SectionDto request){
        return sectionService.createNewSection(courseId,request);
    }
}
