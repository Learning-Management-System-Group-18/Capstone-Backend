package com.example.capstone.service;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.constant.AppConstant.*;
import com.example.capstone.constant.BucketName;
import com.example.capstone.domain.common.SearchSpecification;
import com.example.capstone.domain.dao.Category;
import com.example.capstone.domain.dao.Course;
import com.example.capstone.domain.dto.CourseDto;
import com.example.capstone.domain.payload.request.SearchRequest;
import com.example.capstone.repository.CategoryRepository;
import com.example.capstone.repository.CourseRepository;
import com.example.capstone.repository.ReviewRepository;
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

    public ResponseEntity<Object> getAllCourse(Long categoryId, int page, int size) {
        log.info("Executing get all course");
        try {
            Pageable pageable = PageRequest.of(page-1,size);
            Page<Course> courseList;
            if (categoryId == null ) {
                log.info("Category Id is null. Getting all course");
                courseList = courseRepository.findAll(pageable);
            } else {
                log.info("Category Id is not null. Getting all course with category Id : {}", categoryId);
                courseList = courseRepository.findAllByCategoryId(categoryId,pageable);
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

    public ResponseEntity<Object> createNewCourse(Long categoryId, CourseDto request, MultipartFile file){
        log.info("Executing add new course");
        if (courseRepository.existsByTitle(request.getTitle())) {
            log.info("Course with name : {} already exist", request.getTitle());
            return ResponseUtil.build(
                    AppConstant.ResponseCode.BAD_CREDENTIALS,
                    "Course with name already exist",
                    HttpStatus.BAD_REQUEST
            );
        }
        try {
            if (file.isEmpty()) {
                throw new IllegalStateException("Cannot upload empty file");
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
            Optional<Category> category = categoryRepository.findById(categoryId);
            Course course = mapper.map(request, Course.class);
            course.setCategory(category.get());
            course.setImageFileName(fileName);
            course.setUrlBucket(urlBucket);
            course.setUrlImage(urlImage);
            courseRepository.save(course);

            log.info("Successfully added new course");
            return ResponseUtil.build(ResponseCode.SUCCESS, mapper.map(course, CourseDto.class), HttpStatus.OK);
        } catch (Exception e) {
            log.error("An error occurred while trying to add new course. Error : {}", e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
