package com.example.capstone.controller;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.domain.dto.CategoryDto;
import com.example.capstone.domain.dto.CourseDto;
import com.example.capstone.domain.payload.request.SearchRequest;
import com.example.capstone.service.CourseService;
import com.example.capstone.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @PostMapping("/course/search")
    public ResponseEntity<Object> searchCourse(@RequestBody SearchRequest request){
        return courseService.searchCourses(request);
    }

    @GetMapping("/courses")
    public ResponseEntity<Object> getAllCourses(@RequestParam(value = "categoryId",required = false)Long categoryId,
                                                @RequestParam("page")int page,
                                                @RequestParam("size")int size) {
        return courseService.getAllCourse(categoryId,page,size);
    }

    @GetMapping("/course")
    public ResponseEntity<Object> getAllCourses() {
        return courseService.getAll();
    }

    @PostMapping("/admin/course")
    public ResponseEntity<Object> createNewCourse(@RequestParam(value = "categoryId")Long categoryId,
                                                  @RequestBody CourseDto request){
        return courseService.createNewCourse(categoryId,request);
    }
}
