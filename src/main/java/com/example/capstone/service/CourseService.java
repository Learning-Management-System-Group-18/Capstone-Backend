package com.example.capstone.service;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.constant.AppConstant.*;
import com.example.capstone.constant.BucketName;
import com.example.capstone.domain.common.SearchSpecification;
import com.example.capstone.domain.dao.Category;
import com.example.capstone.domain.dao.Course;
import com.example.capstone.domain.dao.Order;
import com.example.capstone.domain.dao.User;
import com.example.capstone.domain.dto.CategoryDto;
import com.example.capstone.domain.dto.CourseDto;
import com.example.capstone.domain.payload.request.SearchRequest;
import com.example.capstone.repository.*;
import com.example.capstone.util.ResponseUtil;
import lombok.AllArgsConstructor;
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

import static org.apache.http.entity.ContentType.*;
import static org.apache.http.entity.ContentType.IMAGE_JPEG;

@Service
@Slf4j
@AllArgsConstructor
public class CourseService {

    private final UploadFileService uploadFileService;

    @Autowired
    CategoryRepository categoryRepository;
    
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CourseRepository courseRepository;

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

    public ResponseEntity<Object> getCourse(Long categoryId, Integer page, Integer size) {
        log.info("Executing get all course with pagination");
        try {
            Pageable pageable = PageRequest.of(page-1,size);
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
                Double rating = reviewRepository.averageOfCourseReviewRating(course.getId());
                CourseDto courseDto = mapper.map(course, CourseDto.class);
                courseDto.setRating(Objects.requireNonNullElse(rating,0.0));
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
                Double rating = reviewRepository.averageOfCourseReviewRating(course.getId());
                CourseDto courseDto = mapper.map(course, CourseDto.class);
                courseDto.setRating(Objects.requireNonNullElse(rating,0.0));
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

            Double rating = reviewRepository.averageOfCourseReviewRating(id);
            CourseDto request = mapper.map(course, CourseDto.class);
            request.setRating(Objects.requireNonNullElse(rating,0.0));

            log.info("Successfully retrieved Course with ID : {}", id);
            return ResponseUtil.build(ResponseCode.SUCCESS,request,HttpStatus.OK);
        } catch (Exception e ) {
            log.error("An error occurred while trying to get Course with ID : {}. Error : {}", id, e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    public ResponseEntity<Object> NewCourse(Long categoryId){
        log.info("Executing create new course");
        try {
            //and then save course in database
            Optional<Category> category = categoryRepository.findById(categoryId);
            Course course = new Course();
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

            //check if the file is an image
            if (!Arrays.asList(IMAGE_PNG.getMimeType(),
                    IMAGE_BMP.getMimeType(),
                    IMAGE_GIF.getMimeType(),
                    IMAGE_JPEG.getMimeType()).contains(file.getContentType())) {
                throw new IllegalStateException("FIle uploaded is not an image");
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

            log.info("Succesfully updated Course Image with Id : [{}]",id);
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
