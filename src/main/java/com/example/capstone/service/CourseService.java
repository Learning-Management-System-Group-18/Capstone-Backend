package com.example.capstone.service;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.constant.AppConstant.*;
import com.example.capstone.constant.BucketName;
import com.example.capstone.domain.common.SearchSpecification;
import com.example.capstone.domain.dao.Category;
import com.example.capstone.domain.dao.Course;
import com.example.capstone.domain.dto.CourseDto;
import com.example.capstone.domain.payload.request.SearchRequest;
import com.example.capstone.repository.*;
import com.example.capstone.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@Slf4j
public class CourseService {

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private OrderRepository orderRepository;

    public ResponseEntity<Object> searchCourse(SearchRequest request){
        log.info("Executing to search course with jpa specification");
        try {
            SearchSpecification<Course> specification = new SearchSpecification<>(request);
            Pageable pageable = SearchSpecification.getPageable(request.getPage(), request.getSize());
            Page<Course> courses = courseRepository.findAll(specification, pageable);
            List<CourseDto> courseDtoList = new ArrayList<>();
            for (Course course: courses){
                Integer countReview = reviewRepository.countAllByCourseId(course.getId());
                Integer countUser = orderRepository.countOrderByCourseIdAndCourseIsDeletedFalse(course.getId());
                Double rating = reviewRepository.averageOfCourseReviewRatingAndCourseIsDeletedFalse(course.getId());
                CourseDto courseDto = mapper.map(course, CourseDto.class);
                courseDto.setRating(Objects.requireNonNullElse(rating,0.0));
                courseDto.setCountUser(Objects.requireNonNullElse(countUser, 0));
                courseDto.setCountReview(Objects.requireNonNullElse(countReview, 0));
                courseDtoList.add(courseDto);
            }
            log.info("Successfully retrieved search course with jpa specification");
            return ResponseUtil.build(ResponseCode.SUCCESS, courseDtoList, HttpStatus.OK);
        } catch (Exception e){
            log.error("An error occurred while trying to search course. Error : {}", e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<Object> popularCourse(){
        try {
            List<Course> courses = courseRepository.popularCourse();
            List<CourseDto> courseDtoList = new ArrayList<>();
            for (Course course: courses){
                Integer countReview = reviewRepository.countAllByCourseId(course.getId());
                Integer countUser = orderRepository.countOrderByCourseIdAndCourseIsDeletedFalse(course.getId());
                Double rating = reviewRepository.averageOfCourseReviewRatingAndCourseIsDeletedFalse(course.getId());
                CourseDto courseDto = mapper.map(course, CourseDto.class);
                courseDto.setRating(Objects.requireNonNullElse(rating,0.0));
                courseDto.setCountUser(Objects.requireNonNullElse(countUser, 0));
                courseDto.setCountReview(Objects.requireNonNullElse(countReview, 0));
                courseDtoList.add(courseDto);
            }
            log.info("Successfully retrieved popular course");
            return ResponseUtil.build(ResponseCode.SUCCESS, courseDtoList, HttpStatus.OK);
        } catch (Exception e){
            log.error("An error occurred while trying to popular course. Error : {}", e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getCourse(Long categoryId, Integer page, Integer size) {
        log.info("Executing get all course with pagination");
        try {
            Pageable pageable = PageRequest.of(page,size);
            Page<Course> courseList;
            if (categoryId == null ) {
                log.info("Category Id is null. Getting all course with pagination");
                courseList = courseRepository.findAll(pageable);
            } else {
                log.info("Category Id is not null. Getting all course with category Id and pagination: {}", categoryId);
                courseList = courseRepository.findAllByCategoryId(categoryId,pageable);
            }

            List<CourseDto> request = new ArrayList<>();
            for (Course course: courseList){
                Integer countReview = reviewRepository.countAllByCourseId(course.getId());
                Integer countUser = orderRepository.countOrderByCourseIdAndCourseIsDeletedFalse(course.getId());
                Double rating = reviewRepository.averageOfCourseReviewRatingAndCourseIsDeletedFalse(course.getId());
                CourseDto courseDto = mapper.map(course, CourseDto.class);
                courseDto.setRating(Objects.requireNonNullElse(rating,0.0));
                courseDto.setCountUser(Objects.requireNonNullElse(countUser, 0));
                courseDto.setCountReview(Objects.requireNonNullElse(countReview, 0));
                request.add(courseDto);
            }
            log.info("Successfully retrieved all course with pagination");
            return ResponseUtil.build(ResponseCode.SUCCESS, request, HttpStatus.OK);
        } catch (Exception e){
            log.error("An error occurred while trying to get all course. Error : {}", e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getAllCourse(Long categoryId) {
        log.info("Executing get all course");
        try {
            List<Course> courseList;
            if (categoryId == null ) {
                log.info("Category Id is null. Getting all course");
                courseList = courseRepository.findAll();
            } else {
                log.info("Category Id is not null. Getting all course with category Id : {}", categoryId);
                courseList = courseRepository.findAllByCategoryId(categoryId);
            }

            List<CourseDto> request = new ArrayList<>();
            for (Course course: courseList){
                Integer countReview = reviewRepository.countAllByCourseId(course.getId());
                Integer countUser = orderRepository.countOrderByCourseIdAndCourseIsDeletedFalse(course.getId());
                Double rating = reviewRepository.averageOfCourseReviewRatingAndCourseIsDeletedFalse(course.getId());
                CourseDto courseDto = mapper.map(course, CourseDto.class);
                courseDto.setRating(Objects.requireNonNullElse(rating,0.0));
                courseDto.setCountUser(Objects.requireNonNullElse(countUser, 0));
                courseDto.setCountReview(Objects.requireNonNullElse(countReview, 0));
                request.add(courseDto);

            }
            log.info("Successfully retrieved all course");
            return ResponseUtil.build(ResponseCode.SUCCESS, request, HttpStatus.OK);
        } catch (Exception e){
            log.error("An error occurred while trying to get all course. Error : {}", e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getCourseById(Long id) {
        log.info("Executing get Course with ID : {}", id);
        try {
            Optional<Course> course = courseRepository.findById(id);
            if (course.isEmpty()) {
                log.info("Course with ID [{}] not found", id);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND,null,HttpStatus.BAD_REQUEST);
            }
            Integer countReview = reviewRepository.countAllByCourseId(id);
            Integer countUser = orderRepository.countOrderByCourseIdAndCourseIsDeletedFalse(id);
            Double rating = reviewRepository.averageOfCourseReviewRatingAndCourseIsDeletedFalse(id);
            CourseDto request = mapper.map(course, CourseDto.class);
            request.setRating(Objects.requireNonNullElse(rating,0.0));
            request.setCountUser(Objects.requireNonNullElse(countUser, 0));
            request.setCountReview(Objects.requireNonNullElse(countReview, 0));

            log.info("Successfully retrieved Course with ID : {}", id);
            return ResponseUtil.build(ResponseCode.SUCCESS,request,HttpStatus.OK);
        } catch (Exception e ) {
            log.error("An error occurred while trying to get Course with ID : {}. Error : {}", id, e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    public ResponseEntity<Object> NewCourse(Long categoryId, CourseDto request){
        log.info("Executing create new course");
        if (courseRepository.existsByTitle(request.getTitle())) {
            log.info("Course with name : {} already exist", request.getTitle());
            return ResponseUtil.build(
                    AppConstant.ResponseCode.BAD_CREDENTIALS,
                    "Course with name already exist",
                    HttpStatus.BAD_REQUEST
            );
        }
        try {

            Optional<Category> category = categoryRepository.findById(categoryId);
            Course course = mapper.map(request, Course.class);
            course.setCategory(category.get());
            courseRepository.save(course);

            log.info("Successfully create new course");
            return ResponseUtil.build(ResponseCode.SUCCESS, mapper.map(course, CourseDto.class), HttpStatus.OK);
        } catch (Exception e) {
            log.error("An error occurred while trying to add new course. Error : {}", e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> updateCourse(Long id, CourseDto request){
        log.info("Executing update existing course");
        try {
            Optional<Course> optionalCourse = courseRepository.findById(id);
            if (optionalCourse.isEmpty()){
                log.info("Course with Id [{}] not found",id);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND,null,HttpStatus.BAD_REQUEST);
            }

            optionalCourse.ifPresent(course -> {
                course.setId(id);
                course.setTitle(request.getTitle());
                course.setDescription(request.getDescription());
                course.setLevel(request.getLevel());
                courseRepository.save(course);
            });

            log.info("Successfully updated Course with Id : [{}]",id);
            return ResponseUtil.build(ResponseCode.SUCCESS,mapper.map(optionalCourse.get(),CourseDto.class),HttpStatus.OK);
        } catch (Exception e){
            log.info("An error occurred while trying to update existing course. Error : {}", e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> updateImageCourse(Long id, MultipartFile file) {
        log.info("Executing update image existing course");
        try {
            Optional<Course> optionalCourse = courseRepository.findById(id);
            if (optionalCourse.isEmpty()){
                log.info("Course with Id [{}] not found",id);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND,null,HttpStatus.BAD_REQUEST);
            }

            //get file metadata
            Map<String, String> metadata = new HashMap<>();
            metadata.put("Content-Type", file.getContentType());
            metadata.put("Content-Length", String.valueOf(file.getSize()));

            //save image in s3
            String pattern = "https://capstone-lms-storage.s3.amazonaws.com";
            String uuid = UUID.randomUUID().toString().replace("-","");
            String urlBucket = String.format("%s/%s/%s", BucketName.CONTENT_IMAGE.getBucketName(), "course-images", uuid);
            String fileName = String.format("%s", file.getOriginalFilename());
            String urlImage = String.format("%s/%s/%s/%s",pattern,"course-images",uuid,fileName);
            uploadFileService.upload(urlBucket, fileName, Optional.of(metadata), file.getInputStream());

            //and then save course in database
            optionalCourse.ifPresent(course -> {
                course.setId(id);
                course.setUrlBucket(urlBucket);
                course.setUrlImage(urlImage);
                course.setImageFileName(fileName);
                courseRepository.save(course);
            });

            log.info("Successfully updated Course Image with Id : [{}]",id);
            return ResponseUtil.build(ResponseCode.SUCCESS,mapper.map(optionalCourse.get(),CourseDto.class),HttpStatus.OK);
        } catch (Exception e) {
            log.info("An error occurred while trying to update existing Course Image. Error : {}", e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteById(Long id){
        log.info("Executing delete existing course");
        try {
            Optional<Course> optionalCourse = courseRepository.findById(id);
            if (optionalCourse.isEmpty()){
                log.info("Course with Id : [{}] is not found",id);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND,null,HttpStatus.BAD_REQUEST);
            }
            Course course = optionalCourse.get();

            if (course.getUrlBucket() == null){
                courseRepository.delete(course);
                log.info("Successfully delete course with ID : [{}]", id);
                return ResponseUtil.build(ResponseCode.SUCCESS,null,HttpStatus.OK);
            }

            uploadFileService.delete(course.getUrlBucket(),course.getImageFileName());
            courseRepository.delete(course);

            log.info("Successfully delete course with ID : [{}]", id);
            return ResponseUtil.build(ResponseCode.SUCCESS,null,HttpStatus.OK);
        } catch (Exception e){
            log.info("An error occurred while trying to delete existing course. Error : {}",e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
