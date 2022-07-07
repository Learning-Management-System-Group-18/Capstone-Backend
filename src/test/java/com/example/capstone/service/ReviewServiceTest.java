package com.example.capstone.service;

import com.example.capstone.domain.common.ApiResponse;
import com.example.capstone.domain.dao.Course;
import com.example.capstone.domain.dao.Review;
import com.example.capstone.domain.dao.User;
import com.example.capstone.domain.dto.ReviewDto;
import com.example.capstone.repository.CourseRepository;
import com.example.capstone.repository.ReviewRepository;
import com.example.capstone.repository.UserRepository;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ReviewService.class)
class ReviewServiceTest {
    @MockBean
    private ModelMapper mapper;

    @MockBean
    private ReviewRepository reviewRepository;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ReviewService reviewService;

    @Test
    void getReviewCourseId_NullRating_Success() {
        List<Review> reviews = new ArrayList<>();
        Review review = Review.builder()
                .review("Test Review")
                .rating(3)
                .build();
        reviews.add(review);
        ReviewDto reviewDto = ReviewDto.builder()
                .review("Test Review")
                .rating(3)
                .build();

        Pageable pageable = PageRequest.of(0, 1);
        Page<Review> reviewPage = new PageImpl<Review>(reviews.subList(0,1), pageable, reviews.size());

        when(reviewRepository.findAllByCourseId(anyLong(), any(Pageable.class))).thenReturn(reviewPage);
        when(mapper.map(any(), eq(ReviewDto.class))).thenReturn(reviewDto);

        ResponseEntity<Object> responseEntity = reviewService.getReviewByCourseId(1L,null,0,1);
        ApiResponse apiResponse = ((ApiResponse) responseEntity.getBody());

        List<ReviewDto> result = ((List<ReviewDto>) apiResponse.getData());

        assertNotNull(apiResponse);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Test Review", result.get(0).getReview());
        assertEquals(3, result.get(0).getRating());

    }

    @Test
    void getReviewCourseId_WithRating_Success() {
        List<Review> reviews = new ArrayList<>();
        Review review = Review.builder()
                .review("Test Review")
                .rating(3)
                .build();
        reviews.add(review);
        ReviewDto reviewDto = ReviewDto.builder()
                .review("Test Review")
                .rating(3)
                .build();

        Pageable pageable = PageRequest.of(0, 1);
        Page<Review> reviewPage = new PageImpl<Review>(reviews.subList(0,1), pageable, reviews.size());

        when(reviewRepository.findAllByCourseIdAndRating(anyLong(),anyInt(), any(Pageable.class))).thenReturn(reviewPage);
        when(mapper.map(any(), eq(ReviewDto.class))).thenReturn(reviewDto);

        ResponseEntity<Object> responseEntity = reviewService.getReviewByCourseId(1L,3,0,1);
        ApiResponse apiResponse = ((ApiResponse) responseEntity.getBody());

        List<ReviewDto> result = ((List<ReviewDto>) apiResponse.getData());

        assertNotNull(apiResponse);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Test Review", result.get(0).getReview());
        assertEquals(3, result.get(0).getRating());

    }

    @Test
    void getReview_Error(){
        when(reviewRepository.findAllByCourseId(anyLong(), any(Pageable.class))).thenThrow(NullPointerException.class);

        ResponseEntity<Object> responseEntity = reviewService.getReviewByCourseId(1L,null, 0,1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void createReview_Success() {
        Course course = Course.builder()
                .id(1L)
                .title("Go-Lang")
                .description("Go-Lang")
                .urlImage("http://images.png")
                .build();

        User user = User.builder()
                .id(1L)
                .email("ilham@gmail.com")
                .build();

        Review review = Review.builder()
                .review("Review")
                .rating(4)
                .course(course)
                .build();

        ReviewDto reviewDto = ReviewDto.builder()
                .review("Review")
                .rating(4)
                .build();

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
        when(mapper.map(any(), eq(Review.class))).thenReturn(review);
        when(reviewRepository.save(any())).thenReturn(review);
        when(mapper.map(any(), eq(ReviewDto.class))).thenReturn(reviewDto);

        ResponseEntity<Object> responseEntity = reviewService.createReview(reviewDto, "email",1L);
        ApiResponse apiResponse = ((ApiResponse) responseEntity.getBody());

        assertNotNull(apiResponse);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Review", ((ReviewDto) apiResponse.getData()).getReview());
        assertEquals(4, ((ReviewDto) apiResponse.getData()).getRating());
    }

    @Test
    void createReview_CourseEmpty() {
        ReviewDto reviewDto = ReviewDto.builder()
                .review("Review")
                .rating(4)
                .build();

        User user = User.builder()
                .id(1L)
                .email("ilham@gmail.com")
                .build();

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Object> responseEntity = reviewService.createReview(reviewDto, "email",1L);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void createReview_UserEmpty() {
        ReviewDto reviewDto = ReviewDto.builder()
                .review("Review")
                .rating(4)
                .build();

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());

        ResponseEntity<Object> responseEntity = reviewService.createReview(reviewDto, "email",1L);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void createReview_Error() {
        ReviewDto reviewDto = ReviewDto.builder()
                .review("Review")
                .rating(4)
                .build();

        when(userRepository.findUserByEmail(anyString())).thenThrow(NullPointerException.class);

        ResponseEntity<Object> responseEntity = reviewService.createReview(reviewDto, "email",1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
}