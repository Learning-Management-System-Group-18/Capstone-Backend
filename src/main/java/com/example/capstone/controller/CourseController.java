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
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/")
@CrossOrigin
public class CourseController {
    @Autowired
    private CourseService courseService;

    @PostMapping("/course/search")
    public ResponseEntity<Object> searchCourse(@RequestBody(required = false) SearchRequest request){
        return courseService.searchCourse(request);
    }

    @GetMapping("/course/popular")
    public ResponseEntity<Object> popularCourse(){
        return courseService.popularCourse();
    }

    @GetMapping("/courses")
    public ResponseEntity<Object> getAllCourses(@RequestParam(value = "categoryId",required = false)Long categoryId,
                                                @RequestParam(value = "page",required = false)Integer page,
                                                @RequestParam(value = "size",required = false)Integer size) {
        if (page == null || size == null){
            return courseService.getAllCourse(categoryId);
        }
        return courseService.getCourse(categoryId,page,size);
    }

    @GetMapping("/course")
    public ResponseEntity<Object> getOneCourse(@RequestParam(value = "id") Long id) {
        return courseService.getCourseById(id);
    }

    @PostMapping(path = "/admin/course")
    public ResponseEntity<Object> createNewCourse(@RequestParam(value = "categoryId") Long categoryId,
                                                  @RequestBody CourseDto request){
        return courseService.NewCourse(categoryId,request);
    }

    @PutMapping( path = "/admin/course")
    public ResponseEntity<Object> updateCourse(@RequestParam(value = "id") Long id,
                                               @RequestBody CourseDto request){
        return courseService.updateCourse(id,request);

    }

    @PutMapping( path = "/admin/course/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateImageCourse(@RequestParam(value = "id") Long id,
                                                    @RequestParam("image") MultipartFile file){
        return courseService.updateImageCourse(id,file);

    }

    @DeleteMapping("/admin/course")
    public ResponseEntity<Object> deleteCourse(@RequestParam(value = "id") Long id){
        return courseService.deleteById(id);
    }
}
