package com.example.capstone.service;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.domain.common.SearchSpecification;
import com.example.capstone.domain.dao.Course;
import com.example.capstone.domain.dao.Review;
import com.example.capstone.domain.dao.User;
import com.example.capstone.domain.dto.CourseDto;
import com.example.capstone.domain.dto.ReviewDto;
import com.example.capstone.domain.payload.request.SearchRequest;
import com.example.capstone.repository.CourseRepository;
import com.example.capstone.repository.ReviewRepository;
import com.example.capstone.repository.UserRepository;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class ReviewService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<Object> getReviewByCourseId(Long courseId, Integer rating,  int page, int size) {
        log.info("Executing get all Review by Course ID [{}]", courseId);
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Review> reviews;
            if (rating == null) {
                log.info("Rating is null. Getting all reviews");
                reviews = reviewRepository.findAllByCourseIdAndCourseIsDeletedFalse(courseId, pageable);
            } else {
                log.info("Rating is not null. Getting reviews by rating");
                reviews = reviewRepository.findAllByCourseIdAndRatingAndCourseIsDeletedFalse(courseId,rating,pageable);
            }

            List<ReviewDto> reviewRequests = new ArrayList<>();
            for (Review review :
                    reviews) {
                ReviewDto request = mapper.map(review, ReviewDto.class);
                reviewRequests.add(request);
            }

            log.info("Successfully retrieved Reviews by Course ID [{}]", courseId);
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS,
                    reviewRequests,
                    HttpStatus.OK);

        } catch (Exception e) {
            log.error("An error occurred while trying to get Reviews by Course ID [{}]. Error : {}",
                    courseId,
                    e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,
                    null,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> createReview(ReviewDto request, String email, Long courseId){
        log.info("Executing create new review");
        try{
            Optional<User> optionalUser = userRepository.findUserByEmail(email);
            if(optionalUser.isEmpty()) {
                log.info("User with Email [{}] not found ", email);
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND,
                        null,
                        HttpStatus.BAD_REQUEST);
            }



            Optional<Course> optionalCourse = courseRepository.findById(courseId);
            if(optionalCourse.isEmpty()) {
                log.info("Course with ID [{}] not found ", courseId);
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND,
                        null,
                        HttpStatus.BAD_REQUEST);
            }

            Review review = mapper.map(request, Review.class);
            review.setCourse(optionalCourse.get());
            review.setUser(optionalUser.get());
            review.setReviewDate(LocalDateTime.now());
            reviewRepository.save(review);
            log.info("Successfully added Review");
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS,
                    mapper.map(review, ReviewDto.class),
                    HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error occured while trying to add review from User {} to Course with ID {}. Error : {} ",
                    email, courseId, e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

















