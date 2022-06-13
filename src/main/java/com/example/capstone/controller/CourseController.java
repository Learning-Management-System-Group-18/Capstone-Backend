package com.example.capstone.controller;

import com.example.capstone.domain.dto.CourseDto;
import com.example.capstone.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping("")
    public ResponseEntity<Object> getAllCourses() {
        return courseService.getAll();
    }

    @PostMapping("")
    public ResponseEntity<Object> createNewCourse(@RequestParam(value = "categoryId")Long categoryId,
                                                  @RequestBody CourseDto request){
        return courseService.createNewCourse(categoryId,request);
    }
}
