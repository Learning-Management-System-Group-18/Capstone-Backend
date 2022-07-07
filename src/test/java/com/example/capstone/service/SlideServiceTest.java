package com.example.capstone.service;

import com.amazonaws.services.dynamodbv2.xspec.S;
import com.example.capstone.constant.AppConstant;
import com.example.capstone.domain.common.ApiResponse;
import com.example.capstone.domain.dao.*;
import com.example.capstone.domain.dto.SlideDto;
import com.example.capstone.domain.dto.VideoDto;
import com.example.capstone.repository.*;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SlideService.class)
class SlideServiceTest {
    @MockBean
    private SectionRepository sectionRepository;

    @MockBean
    private ModelMapper mapper;

    @MockBean
    private SlideRepository slideRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private SlideCompletedRepository slideCompletedRepository;

    @Autowired
    private SlideService slideService;

    @Test
    void completeSlideById_Success() {
        Course course = Course.builder()
                .id(1L)
                .build();

        Section section = Section.builder()
                .id(1L)
                .course(course)
                .build();

        User user = User.builder()
                .id(1L)
                .email("ilham@gmail.com")
                .build();


        Slide slide = Slide.builder()
                .id(1L)
                .title("Slide")
                .description("description")
                .section(section)
                .link("link")
                .build();

        Boolean order = true;

        Boolean isCompleted = false;

        SlideDto slideDto = SlideDto.builder()
                .id(1L)
                .title("Slide")
                .description("description")
                .completed(isCompleted)
                .link("link")
                .build();

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(slideRepository.findById(anyLong())).thenReturn(Optional.of(slide));
        when(orderRepository.existsByCourseIdAndUserId(anyLong(),anyLong())).thenReturn(order);
        when(mapper.map(any(), eq(SlideDto.class))).thenReturn(slideDto);
        when(slideCompletedRepository.existsByUserIdAndSlideId(anyLong(),anyLong())).thenReturn(isCompleted);

        ResponseEntity<Object> responseEntity = slideService.completeSlide(1L,"email");
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());


    }

    @Test
    void completeSlideById_AlreadyCompleted() {
        Course course = Course.builder()
                .id(1L)
                .build();

        Section section = Section.builder()
                .id(1L)
                .course(course)
                .build();

        User user = User.builder()
                .id(1L)
                .email("ilham@gmail.com")
                .build();


        Slide slide = Slide.builder()
                .id(1L)
                .title("Slide")
                .description("description")
                .section(section)
                .link("link")
                .build();

        Boolean order = true;

        Boolean isCompleted = true;

        SlideDto slideDto = SlideDto.builder()
                .id(1L)
                .title("Slide")
                .description("description")
                .link("link")
                .build();

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(slideRepository.findById(anyLong())).thenReturn(Optional.of(slide));
        when(orderRepository.existsByCourseIdAndUserId(anyLong(),anyLong())).thenReturn(order);
        when(mapper.map(any(), eq(SlideDto.class))).thenReturn(slideDto);
        when(slideCompletedRepository.existsByUserIdAndSlideId(anyLong(),anyLong())).thenReturn(isCompleted);

        ResponseEntity<Object> responseEntity = slideService.completeSlide(1L,"email");
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(AppConstant.ResponseCode.ALREADY_COMPLETED.getCode(), apiResponse.getStatus().getCode());

    }

    @Test
    void completeSlideById_UserEmpty() {
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());

        ResponseEntity responseEntity = slideService.completeSlide(1L,"email");

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

    @Test
    void completeSlideById_SlideEmpty() {
        User user = User.builder()
                .id(1L)
                .email("ilham@gmail.com")
                .build();

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));

        when(slideRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity responseEntity = slideService.completeSlide(1L,"email");

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void completeSlideById_NotEnroll() {
        Course course = Course.builder()
                .id(1L)
                .build();

        Section section = Section.builder()
                .id(1L)
                .course(course)
                .build();

        User user = User.builder()
                .id(1L)
                .email("ilham@gmail.com")
                .build();


        Slide slide = Slide.builder()
                .id(1L)
                .title("Slide")
                .description("description")
                .section(section)
                .link("link")
                .build();

        Boolean order = false;

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(slideRepository.findById(anyLong())).thenReturn(Optional.of(slide));
        when(orderRepository.existsByCourseIdAndUserId(anyLong(),anyLong())).thenReturn(order);

        ResponseEntity responseEntity = slideService.completeSlide(1L,"email");

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());

    }

    @Test
    void completeSlideById_Error() {
        when(userRepository.findUserByEmail(anyString())).thenThrow(NullPointerException.class);
        ResponseEntity responseEntity = slideService.completeSlide(1L,"email");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void createNewSlide_Success() {
        Course course = Course.builder()
                .id(1L)
                .build();

        Section section = Section.builder()
                .id(1L)
                .course(course)
                .build();

        Slide slide = Slide.builder()
                .id(1L)
                .title("Slide")
                .description("description")
                .section(section)
                .link("link")
                .build();

        SlideDto slideDto = SlideDto.builder()
                .id(1L)
                .title("Slide")
                .description("description")
                .link("link")
                .build();

        when(sectionRepository.findById(anyLong())).thenReturn(Optional.of(section));
        when(mapper.map(any(), eq(Slide.class))).thenReturn(slide);
        when(slideRepository.save(any())).thenReturn(slide);
        when(mapper.map(any(), eq(SlideDto.class))).thenReturn(slideDto);

        ResponseEntity<Object> responseEntity = slideService.createNewSlide(1L, slideDto);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Slide", ((SlideDto) apiResponse.getData()).getTitle());

    }

    @Test
    void createNewSlide_SectionEmpty() {
        SlideDto slideDto = SlideDto.builder()
                .id(1L)
                .title("Slide")
                .description("description")
                .link("link")
                .build();

        when(sectionRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = slideService.createNewSlide(1L, slideDto);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

    @Test
    void createNewSlide_Error() {
        SlideDto slideDto = SlideDto.builder()
                .id(1L)
                .title("Slide")
                .description("description")
                .link("link")
                .build();

        when(sectionRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = slideService.createNewSlide(1L, slideDto);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(AppConstant.ResponseCode.UNKNOWN_ERROR.getCode(), apiResponse.getStatus().getCode());
    }

    @Test
    void getSlideById_Success() {
        Course course = Course.builder()
                .id(1L)
                .build();

        Section section = Section.builder()
                .id(1L)
                .course(course)
                .build();

        User user = User.builder()
                .id(1L)
                .email("ilham@gmail.com")
                .build();


        Slide slide = Slide.builder()
                .id(1L)
                .title("Slide")
                .description("description")
                .section(section)
                .link("link")
                .build();

        Boolean order = true;

        Boolean isCompleted = true;

        SlideDto slideDto = SlideDto.builder()
                .id(1L)
                .title("Slide")
                .description("description")
                .link("link")
                .build();



        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(slideRepository.findById(anyLong())).thenReturn(Optional.of(slide));
        when(orderRepository.existsByCourseIdAndUserId(anyLong(),anyLong())).thenReturn(order);
        when(mapper.map(any(), eq(SlideDto.class))).thenReturn(slideDto);
        when(slideCompletedRepository.existsByUserIdAndSlideId(anyLong(),anyLong())).thenReturn(isCompleted);


        ResponseEntity responseEntity = slideService.getSlideById(1L,"email");
        ApiResponse apiResponse = ((ApiResponse) responseEntity.getBody());


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Slide", ((SlideDto) apiResponse.getData()).getTitle());
    }

    @Test
    void getSlideById_UserEmpty() {
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());

        ResponseEntity responseEntity = slideService.getSlideById(1L,"email");

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

    @Test
    void getSlideById_SlideEmpty() {
        User user = User.builder()
                .id(1L)
                .email("ilham@gmail.com")
                .build();

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));

        when(slideRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity responseEntity = slideService.getSlideById(1L,"email");

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

    @Test
    void getSlideById_NotEnroll() {
        Course course = Course.builder()
                .id(1L)
                .build();

        Section section = Section.builder()
                .id(1L)
                .course(course)
                .build();

        User user = User.builder()
                .id(1L)
                .email("ilham@gmail.com")
                .build();


        Slide slide = Slide.builder()
                .id(1L)
                .title("Slide")
                .description("description")
                .section(section)
                .link("link")
                .build();

        Boolean order = false;

        SlideDto slideDto = SlideDto.builder()
                .id(1L)
                .title("Slide")
                .description("description")
                .link("link")
                .build();


        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(slideRepository.findById(anyLong())).thenReturn(Optional.of(slide));
        when(orderRepository.existsByCourseIdAndUserId(anyLong(),anyLong())).thenReturn(order);
        when(mapper.map(any(), eq(SlideDto.class))).thenReturn(slideDto);


        ResponseEntity responseEntity = slideService.getSlideById(1L,"email");
        ApiResponse apiResponse = ((ApiResponse) responseEntity.getBody());

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    void getSlideById_Error() {
        when(userRepository.findUserByEmail(anyString())).thenThrow(NullPointerException.class);
        ResponseEntity responseEntity = slideService.getSlideById(1L,"email");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

    }

    @Test
    void updateById_Success() {
        Section section = Section.builder()
                .id(1L)
                .build();

        Slide slide = Slide.builder()
                .id(1L)
                .title("Slide")
                .section(section)
                .build();

        SlideDto slideDto = SlideDto.builder()
                .id(1L)
                .title("Slide")
                .build();

        when(slideRepository.findById(anyLong())).thenReturn(Optional.of(slide));
        when(sectionRepository.findById(anyLong())).thenReturn(Optional.of(section));
        when(mapper.map(any(),eq(Slide.class))).thenReturn(slide);
        when(mapper.map(any(),eq(SlideDto.class))).thenReturn(slideDto);
        when(slideRepository.save(any())).thenReturn(slide);

        ResponseEntity responseEntity = slideService.updateSlide(1L,slideDto);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Slide", ((SlideDto) apiResponse.getData()).getTitle());

    }

    @Test
    void updateById_SlideEmpty() {
        SlideDto slideDto = SlideDto.builder()
                .id(1L)
                .title("Slide")
                .build();

        when(slideRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity responseEntity = slideService.updateSlide(1L,slideDto);

        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        assertEquals(AppConstant.ResponseCode.DATA_NOT_FOUND.getCode(), apiResponse.getStatus().getCode());

    }

    @Test
    void updateById_Error() {
        SlideDto slideDto = SlideDto.builder()
                .id(1L)
                .title("Slide")
                .build();

        when(slideRepository.findById(anyLong())).thenThrow(NullPointerException.class);

        ResponseEntity responseEntity = slideService.updateSlide(1L,slideDto);

        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        assertEquals(AppConstant.ResponseCode.UNKNOWN_ERROR.getCode(), apiResponse.getStatus().getCode());

    }

    @Test
    void deleteById_Success() {
        when(slideRepository.findById(anyLong())).thenReturn(Optional.of(Slide
                .builder()
                .id(1L)
                .build()));

        doNothing().when(slideRepository).delete(any());

        ResponseEntity<Object> responseEntity = slideService.deleteById(1L);
        verify(slideRepository, times(1)).delete(any());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void deleteById_SlideEmpty() {
        when(slideRepository.findById(anyLong())).thenReturn(Optional.empty());


        ResponseEntity<Object> responseEntity = slideService.deleteById(1L);

        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        assertEquals(AppConstant.ResponseCode.DATA_NOT_FOUND.getCode(), apiResponse.getStatus().getCode());
    }

    @Test
    void deleteById_Error() {
        when(slideRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = slideService.deleteById(1L);

        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        assertEquals(AppConstant.ResponseCode.UNKNOWN_ERROR.getCode(), apiResponse.getStatus().getCode());
    }

}