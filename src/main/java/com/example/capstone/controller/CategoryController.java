package com.example.capstone.controller;

import com.example.capstone.domain.dto.CategoryDto;
import com.example.capstone.domain.dto.CourseDto;
import com.example.capstone.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("")
    public ResponseEntity<Object> createNewCategory(@RequestBody CategoryDto request){
        return categoryService.addNew(request);
    }
}
