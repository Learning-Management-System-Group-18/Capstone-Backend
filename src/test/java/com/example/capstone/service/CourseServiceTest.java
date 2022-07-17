package com.example.capstone.service;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.domain.common.ApiResponse;
import com.example.capstone.domain.dao.Category;
import com.example.capstone.domain.dao.Course;
import com.example.capstone.domain.dto.CourseDto;
import com.example.capstone.domain.payload.request.SearchRequest;
import com.example.capstone.domain.payload.response.CategoryResponse;
import com.example.capstone.repository.CategoryRepository;
import com.example.capstone.repository.CourseRepository;
import com.example.capstone.repository.OrderRepository;
import com.example.capstone.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CourseService.class)
class CourseServiceTest {

    @MockBean
    private UploadFileService uploadFileService;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private ModelMapper mapper;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private ReviewRepository reviewRepository;

    @MockBean
    private OrderRepository orderRepository;

    @Autowired
    private CourseService courseService;

    @Test
    void searchCourse_Success() {
        List<Course> courseList = new ArrayList<>();

        Course course =  Course.builder()
                .id(1L)
                .title("Course A")
                .description("Description")
                .build();

        SearchRequest searchRequest = SearchRequest.builder()
                .build();

        courseList.add(course);

        Pageable pageable = PageRequest.of(0,1);
        Page<Course> coursePage = new PageImpl<Course>(courseList.subList(0,1),pageable, courseList.size());

        Double rating = 3.1;

        Integer countUser = 3;

        Integer countReview = 3;

        CourseDto courseDto = CourseDto.builder()
                .id(1L)
                .title("Course A")
                .description("Description")
                .build();

        when(courseRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(coursePage);
        when(reviewRepository.countAllByCourseId(anyLong())).thenReturn(countReview);
        when(orderRepository.countOrderByCourseIdAndCourseIsDeletedFalse(anyLong())).thenReturn(countUser);
        when(reviewRepository.averageOfCourseReviewRatingAndCourseIsDeletedFalse(anyLong())).thenReturn(rating);
        when(mapper.map(any(),eq(CourseDto.class))).thenReturn(courseDto);

        ResponseEntity<Object> responseEntity = courseService.searchCourse(searchRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        List<CourseDto> result = ((List<CourseDto>) apiResponse.getData());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Course A", result.get(0).getTitle());

    }

    @Test
    void searchCourse_Error() {
        SearchRequest searchRequest = SearchRequest.builder()
                .build();

        when(courseRepository.findAll(any(Specification.class), any(Pageable.class))).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = courseService.searchCourse(searchRequest);
        ApiResponse apiResponse = ((ApiResponse) responseEntity.getBody());
        assertEquals(AppConstant.ResponseCode.UNKNOWN_ERROR.getCode(), apiResponse.getStatus().getCode());

    }

    @Test
    void popularCourse_Success() {
        List<Course> courseList = new ArrayList<>();

        Course course =  Course.builder()
                .id(1L)
                .title("Course A")
                .description("Description")
                .build();

        courseList.add(course);

        Double rating = 3.1;

        Integer countUser = 3;

        Integer countReview = 4;

        CourseDto courseDto = CourseDto.builder()
                .id(1L)
                .title("Course A")
                .description("Description")
                .build();
        when(courseRepository.popularCourse()).thenReturn(courseList);
        when(reviewRepository.countAllByCourseId(anyLong())).thenReturn(countReview);
        when(orderRepository.countOrderByCourseIdAndCourseIsDeletedFalse(anyLong())).thenReturn(countUser);
        when(reviewRepository.averageOfCourseReviewRatingAndCourseIsDeletedFalse(anyLong())).thenReturn(rating);
        when(mapper.map(any(),eq(CourseDto.class))).thenReturn(courseDto);

        ResponseEntity<Object> responseEntity = courseService.popularCourse();
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        List<CourseDto> result = ((List<CourseDto>) apiResponse.getData());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Course A", result.get(0).getTitle());



    }

    @Test
    void popularCourse_Error() {
        when(courseRepository.popularCourse()).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = courseService.popularCourse();
        ApiResponse apiResponse = ((ApiResponse) responseEntity.getBody());
        assertEquals(AppConstant.ResponseCode.UNKNOWN_ERROR.getCode(), apiResponse.getStatus().getCode());

    }

    @Test
    void getCourseByCategoryId_WithPagination_Success() {
        List<Course> courseList = new ArrayList<>();

        Course course =  Course.builder()
                .id(1L)
                .title("Course A")
                .description("Description")
                .build();


        courseList.add(course);

        Pageable pageable = PageRequest.of(0,1);
        Page<Course> coursePage = new PageImpl<Course>(courseList.subList(0,1),pageable, courseList.size());

        Double rating = 3.1;

        Integer countUser = 3;

        Integer countReview = 3;

        CourseDto courseDto = CourseDto.builder()
                .id(1L)
                .title("Course A")
                .description("Description")
                .build();

        when(courseRepository.findAllByCategoryId(anyLong(),any())).thenReturn(coursePage);
        when(reviewRepository.countAllByCourseId(anyLong())).thenReturn(countReview);
        when(orderRepository.countOrderByCourseIdAndCourseIsDeletedFalse(anyLong())).thenReturn(countUser);
        when(reviewRepository.averageOfCourseReviewRatingAndCourseIsDeletedFalse(anyLong())).thenReturn(rating);
        when(mapper.map(any(),eq(CourseDto.class))).thenReturn(courseDto);

        ResponseEntity<Object> responseEntity = courseService.getCourse(1L,0,1);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        List<CourseDto> result = ((List<CourseDto>) apiResponse.getData());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Course A", result.get(0).getTitle());


    }

    @Test
    void getCourseNullCategoryId_WithPagination_Success() {
        List<Course> courseList = new ArrayList<>();

        Course course =  Course.builder()
                .id(1L)
                .title("Course A")
                .description("Description")
                .build();


        courseList.add(course);

        Pageable pageable = PageRequest.of(0,1);
        Page<Course> coursePage = new PageImpl<Course>(courseList.subList(0,1),pageable, courseList.size());

        Double rating = 3.1;

        Integer countUser = 3;

        Integer countReview = 3;

        CourseDto courseDto = CourseDto.builder()
                .id(1L)
                .title("Course A")
                .description("Description")
                .build();

        when(courseRepository.findAll(any(Pageable.class))).thenReturn(coursePage);
        when(reviewRepository.countAllByCourseId(anyLong())).thenReturn(countReview);
        when(orderRepository.countOrderByCourseIdAndCourseIsDeletedFalse(anyLong())).thenReturn(countUser);
        when(reviewRepository.averageOfCourseReviewRatingAndCourseIsDeletedFalse(anyLong())).thenReturn(rating);
        when(mapper.map(any(),eq(CourseDto.class))).thenReturn(courseDto);

        ResponseEntity<Object> responseEntity = courseService.getCourse(null,0,1);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        List<CourseDto> result = ((List<CourseDto>) apiResponse.getData());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Course A", result.get(0).getTitle());
    }

    @Test
    void getCourse_Error() {
        when(courseRepository.findAll(any(Pageable.class))).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = courseService.getCourse(1L,0,1);
        ApiResponse apiResponse = ((ApiResponse) responseEntity.getBody());
        assertEquals(AppConstant.ResponseCode.UNKNOWN_ERROR.getCode(), apiResponse.getStatus().getCode());

    }

    @Test
    void getAllCourseByCategoryId_WithoutPagination_Success() {
        List<Course> courseList = new ArrayList<>();

        Course course =  Course.builder()
                .id(1L)
                .title("Course A")
                .description("Description")
                .build();

        courseList.add(course);

        Double rating = 3.1;

        Integer countUser = 3;

        Integer countReview = 3;

        CourseDto courseDto = CourseDto.builder()
                .id(1L)
                .title("Course A")
                .description("Description")
                .build();

        when(courseRepository.findAllByCategoryId(anyLong())).thenReturn(courseList);
        when(reviewRepository.countAllByCourseId(anyLong())).thenReturn(countReview);
        when(orderRepository.countOrderByCourseIdAndCourseIsDeletedFalse(anyLong())).thenReturn(countUser);
        when(reviewRepository.averageOfCourseReviewRatingAndCourseIsDeletedFalse(anyLong())).thenReturn(rating);
        when(mapper.map(any(),eq(CourseDto.class))).thenReturn(courseDto);

        ResponseEntity<Object> responseEntity = courseService.getAllCourse(1L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        List<CourseDto> result = ((List<CourseDto>) apiResponse.getData());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Course A", result.get(0).getTitle());


    }

    @Test
    void getAllCourseNullCategoryId_WithoutPagination_Success() {
        List<Course> courseList = new ArrayList<>();

        Course course =  Course.builder()
                .id(1L)
                .title("Course A")
                .description("Description")
                .build();

        courseList.add(course);

        Double rating = 3.1;

        Integer countUser = 3;

        Integer countReview = 3;

        CourseDto courseDto = CourseDto.builder()
                .id(1L)
                .title("Course A")
                .description("Description")
                .build();

        when(courseRepository.findAll()).thenReturn(courseList);
        when(reviewRepository.countAllByCourseId(anyLong())).thenReturn(countReview);
        when(orderRepository.countOrderByCourseIdAndCourseIsDeletedFalse(anyLong())).thenReturn(countUser);
        when(reviewRepository.averageOfCourseReviewRatingAndCourseIsDeletedFalse(anyLong())).thenReturn(rating);
        when(mapper.map(any(),eq(CourseDto.class))).thenReturn(courseDto);

        ResponseEntity<Object> responseEntity = courseService.getAllCourse(null);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        List<CourseDto> result = ((List<CourseDto>) apiResponse.getData());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Course A", result.get(0).getTitle());
    }

    @Test
    void getAllCourse_Error() {
        when(courseRepository.findAll()).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = courseService.getAllCourse(null);
        ApiResponse apiResponse = ((ApiResponse) responseEntity.getBody());
        assertEquals(AppConstant.ResponseCode.UNKNOWN_ERROR.getCode(), apiResponse.getStatus().getCode());

    }

    @Test
    void getCourseById_Success() {
        Course course =  Course.builder()
                .id(1L)
                .title("Course A")
                .description("Description")
                .build();

        Double rating = 3.1;

        Integer countUser = 3;

        Integer countReview = 3;

        CourseDto courseDto = CourseDto.builder()
                .id(1L)
                .title("Course A")
                .description("Description")
                .build();

        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
        when(reviewRepository.countAllByCourseId(anyLong())).thenReturn(countReview);
        when(orderRepository.countOrderByCourseIdAndCourseIsDeletedFalse(anyLong())).thenReturn(countUser);
        when(reviewRepository.averageOfCourseReviewRatingAndCourseIsDeletedFalse(anyLong())).thenReturn(rating);
        when(mapper.map(any(),eq(CourseDto.class))).thenReturn(courseDto);

        ResponseEntity<Object> responseEntity = courseService.getCourseById(1L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Course A", ((CourseDto) apiResponse.getData()).getTitle());


    }

    @Test
    void getCourseById_CourseEmpty() {
        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = courseService.getCourseById(1L);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void getCourseById_Error() {
        when(courseRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = courseService.getCourseById(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void newCourse_Success() {
        Category category = Category.builder()
                .id(1L)
                .title("Category")
                .build();

        CategoryResponse categoryResponse = CategoryResponse.builder()
                .id(1L)
                .title("Category")
                .build();

        Course course =  Course.builder()
                .id(1L)
                .title("Course A")
                .description("Description")
                .category(category)
                .build();

        CourseDto courseDto = CourseDto.builder()
                .id(1L)
                .title("Course A")
                .description("Description")
                .category(categoryResponse)
                .build();

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(mapper.map(any(),eq(Course.class))).thenReturn(course);
        when(courseRepository.save(any())).thenReturn(course);
        when(mapper.map(any(),eq(CourseDto.class))).thenReturn(courseDto);
        ResponseEntity<Object> responseEntity = courseService.NewCourse(1L,courseDto);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Course A", ((CourseDto) apiResponse.getData()).getTitle());
    }

    @Test
    void newCourse_ExistTitle() {
        CategoryResponse categoryResponse = CategoryResponse.builder()
                .id(1L)
                .title("Category")
                .build();

        CourseDto courseDto = CourseDto.builder()
                .id(1L)
                .title("Course A")
                .description("Description")
                .category(categoryResponse)
                .build();

        Boolean exist = true;

        when(courseRepository.existsByTitle(anyString())).thenReturn(exist);
        ResponseEntity<Object> responseEntity = courseService.NewCourse(1L,courseDto);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());


    }

    @Test
    void newCourse_Error() {
        when(courseRepository.existsByTitle(anyString())).thenThrow(NullPointerException.class);
        CategoryResponse categoryResponse = CategoryResponse.builder()
                .id(1L)
                .title("Category")
                .build();

        CourseDto courseDto = CourseDto.builder()
                .id(1L)
                .title("Course A")
                .description("Description")
                .category(categoryResponse)
                .build();
        ResponseEntity<Object> responseEntity = courseService.NewCourse(1L,courseDto);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

    }

    @Test
    void updateCourse_Success() {
        Category category = Category.builder()
                .id(1L)
                .title("Category")
                .build();

        CategoryResponse categoryResponse = CategoryResponse.builder()
                .id(1L)
                .title("Category")
                .build();

        Course course =  Course.builder()
                .id(1L)
                .title("Course A")
                .description("Description")
                .category(category)
                .build();

        CourseDto courseDto = CourseDto.builder()
                .id(1L)
                .title("Course A")
                .description("Description")
                .category(categoryResponse)
                .build();

        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
        when(courseRepository.save(any())).thenReturn(course);
        when(mapper.map(any(),eq(CourseDto.class))).thenReturn(courseDto);
        ResponseEntity<Object> responseEntity = courseService.updateCourse(1L,courseDto);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Course A", ((CourseDto) apiResponse.getData()).getTitle());


    }

    @Test
    void updateCourse_EmptyCourse() {
        CategoryResponse categoryResponse = CategoryResponse.builder()
                .id(1L)
                .title("Category")
                .build();


        CourseDto courseDto = CourseDto.builder()
                .id(1L)
                .title("Course A")
                .description("Description")
                .category(categoryResponse)
                .build();

        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = courseService.updateCourse(1L,courseDto);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void updateCourse_Error() {
        CategoryResponse categoryResponse = CategoryResponse.builder()
                .id(1L)
                .title("Category")
                .build();


        CourseDto courseDto = CourseDto.builder()
                .id(1L)
                .title("Course A")
                .description("Description")
                .category(categoryResponse)
                .build();

        when(courseRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = courseService.updateCourse(1L,courseDto);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void updateImageCourse_Success() {
        Category category = Category.builder()
                .id(1L)
                .title("Category")
                .build();

        CategoryResponse categoryResponse = CategoryResponse.builder()
                .id(1L)
                .title("Category")
                .build();

        Course course =  Course.builder()
                .id(1L)
                .title("Course A")
                .description("Description")
                .category(category)
                .build();

        CourseDto courseDto = CourseDto.builder()
                .id(1L)
                .title("Course A")
                .description("Description")
                .category(categoryResponse)
                .build();

        MockMultipartFile file = new MockMultipartFile(
                "name",
                "some-name",
                MediaType.TEXT_PLAIN_VALUE,
                "think of this as image".getBytes());

        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
        when(courseRepository.save(any())).thenReturn(course);
        when(mapper.map(any(),eq(CourseDto.class))).thenReturn(courseDto);

        ResponseEntity<Object> responseEntity = courseService.updateImageCourse(1L,file);

        verify(uploadFileService,times(1)).upload(any(),any(),any(),any());

    }

    @Test
    void updateImageCourse_CourseEmpty() {
        MockMultipartFile file = new MockMultipartFile(
                "name",
                "some-name",
                MediaType.TEXT_PLAIN_VALUE,
                "think of this as image".getBytes());

        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = courseService.updateImageCourse(1L,file);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

    @Test
    void updateImageCourse_Error() {
        MockMultipartFile file = new MockMultipartFile(
                "name",
                "some-name",
                MediaType.TEXT_PLAIN_VALUE,
                "think of this as image".getBytes());

        when(courseRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = courseService.updateImageCourse(1L,file);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void deleteById_Success() {
        Course course =  Course.builder()
                .id(1L)
                .title("Course A")
                .description("Description")
                .urlBucket("some")
                .build();

        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));

        ResponseEntity<Object> responseEntity = courseService.deleteById(anyLong());

        verify(uploadFileService, times(1)).delete(any(),any());
        verify(courseRepository, times(1)).delete(course);


    }

    @Test
    void deleteById_CourseEmpty() {
        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = courseService.deleteById(anyLong());
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void deleteById_ImageEmpty_Success() {
        Course course =  Course.builder()
                .id(1L)
                .title("Course A")
                .description("Description")
                .urlBucket(null)
                .build();

        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));

        ResponseEntity<Object> responseEntity = courseService.deleteById(anyLong());


        verify(courseRepository, times(1)).delete(course);
    }


    @Test
    void deleteById_Error() {
        when(courseRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = courseService.deleteById(anyLong());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }
}