package com.example.capstone.service;

import com.example.capstone.constant.AppConstant.*;
import com.example.capstone.constant.BucketName;
import com.example.capstone.domain.common.SearchSpecification;
import com.example.capstone.domain.dao.Category;
import com.example.capstone.domain.dao.Course;
import com.example.capstone.domain.dto.CourseDto;
import com.example.capstone.domain.payload.request.SearchRequest;
import com.example.capstone.repository.CategoryRepository;
import com.example.capstone.repository.CourseRepository;
import com.example.capstone.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CourseService {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CourseRepository courseRepository;

    public ResponseEntity<Object> getAll() {

    @Autowired
    private ReviewRepository reviewRepository;

    public ResponseEntity<Object> searchCourses(SearchRequest request){
        try {
            SearchSpecification<Course> specification = new SearchSpecification<>(request);
            Pageable pageable = SearchSpecification.getPageable(request.getPage()-1, request.getSize() );
            Page<Course> courses = courseRepository.findAll(specification,pageable);
            List<CourseDto> courseDtoList = new ArrayList<>();

            for (Course course: courses){
                Double rating = reviewRepository.averageOfCourseReviewRating(course.getId());
                CourseDto courseDto = mapper.map(course, CourseDto.class);
                courseDto.setRating(Objects.requireNonNullElse(rating,0.0));
                courseDtoList.add(courseDto);
            }
            log.info("Successfully retrieved all course");
            return ResponseUtil.build(ResponseCode.SUCCESS, courseDtoList, HttpStatus.OK);
        } catch (Exception e){
            log.error("An error occurred while trying to get all course. Error : {}", e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getAllCourse(Long categoryId, int page, int size) {

        log.info("Executing get all course");
        try {
            List<Course> courseList = courseRepository.findAll();
            List<CourseDto> courseDtoList = new ArrayList<>();
            for (Course course: courseList){
                courseDtoList.add(mapper.map(course, CourseDto.class));
            }
            log.info("Successfully retrieved all course");
            return ResponseUtil.build(ResponseCode.SUCCESS, courseDtoList, HttpStatus.OK);
        } catch (Exception e){
            log.error("An error occurred while trying to get all course. Error : {}", e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> createNewCourse(Long categoryId,CourseDto request){
        log.info("Executing add new course");
        try {
            Optional<Category> category = categoryRepository.findById(categoryId);
            Course course = mapper.map(request,Course.class);
            course.setCategory(category.get());
            courseRepository.save(course);

            log.info("Successfully added new course");
            return ResponseUtil.build(ResponseCode.SUCCESS, mapper.map(course, CourseDto.class),HttpStatus.OK);
        } catch (Exception e){
            log.error("An Error occurred while trying to add new course. Error:{}",e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
