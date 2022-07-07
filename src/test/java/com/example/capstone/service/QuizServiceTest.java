package com.example.capstone.service;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.domain.common.ApiResponse;
import com.example.capstone.domain.dao.*;
import com.example.capstone.domain.dto.QuizDto;
import com.example.capstone.domain.dto.SlideDto;
import com.example.capstone.domain.dto.VideoDto;
import com.example.capstone.repository.*;
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
@SpringBootTest(classes = QuizService.class)
class QuizServiceTest {

    @MockBean
    private SectionRepository sectionRepository;

    @MockBean
    private ModelMapper mapper;

    @MockBean
    private QuizRepository quizRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private QuizCompletedRepository quizCompletedRepository;

    @Autowired
    private QuizService quizService;

    @Test
    void completeQuizById_Success() {
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


        Quiz quiz = Quiz.builder()
                .id(1L)
                .title("Quiz")
                .description("description")
                .section(section)
                .link("link")
                .build();

        Boolean order = true;

        Boolean isCompleted = false;

        QuizDto quizDto = QuizDto.builder()
                .id(1L)
                .title("Quiz")
                .description("description")
                .completed(isCompleted)
                .link("link")
                .build();

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(quizRepository.findById(anyLong())).thenReturn(Optional.of(quiz));
        when(orderRepository.existsByCourseIdAndUserId(anyLong(),anyLong())).thenReturn(order);
        when(mapper.map(any(), eq(QuizDto.class))).thenReturn(quizDto);
        when(quizCompletedRepository.existsByUserIdAndQuizId(anyLong(),anyLong())).thenReturn(isCompleted);

        ResponseEntity<Object> responseEntity = quizService.completeQuiz(1L,"email");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());


    }

    @Test
    void completeQuizById_AlreadyCompleted() {
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


        Quiz quiz = Quiz.builder()
                .id(1L)
                .title("Quiz")
                .description("description")
                .section(section)
                .link("link")
                .build();

        Boolean order = true;

        Boolean isCompleted = true;

        QuizDto quizDto = QuizDto.builder()
                .id(1L)
                .title("Quiz")
                .description("description")
                .link("link")
                .build();

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(quizRepository.findById(anyLong())).thenReturn(Optional.of(quiz));
        when(orderRepository.existsByCourseIdAndUserId(anyLong(),anyLong())).thenReturn(order);
        when(mapper.map(any(), eq(QuizDto.class))).thenReturn(quizDto);
        when(quizCompletedRepository.existsByUserIdAndQuizId(anyLong(),anyLong())).thenReturn(isCompleted);

        ResponseEntity<Object> responseEntity = quizService.completeQuiz(1L,"email");
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(AppConstant.ResponseCode.ALREADY_COMPLETED.getCode(), apiResponse.getStatus().getCode());

    }

    @Test
    void completeQuizById_UserEmpty() {
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());

        ResponseEntity responseEntity = quizService.completeQuiz(1L,"email");

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

    @Test
    void completeQuizById_QuizEmpty() {
        User user = User.builder()
                .id(1L)
                .email("ilham@gmail.com")
                .build();

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));

        when(quizRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity responseEntity = quizService.completeQuiz(1L,"email");

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void completeQuizById_NotEnroll() {
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


        Quiz quiz = Quiz.builder()
                .id(1L)
                .title("Video")
                .description("description")
                .section(section)
                .link("link")
                .build();

        Boolean order = false;


        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(quizRepository.findById(anyLong())).thenReturn(Optional.of(quiz));
        when(orderRepository.existsByCourseIdAndUserId(anyLong(),anyLong())).thenReturn(order);

        ResponseEntity responseEntity = quizService.completeQuiz(1L,"email");

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());

    }

    @Test
    void completeQuizById_Error() {
        when(userRepository.findUserByEmail(anyString())).thenThrow(NullPointerException.class);
        ResponseEntity responseEntity = quizService.completeQuiz(1L,"email");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void createNewQuiz_Success() {
        Course course = Course.builder()
                .id(1L)
                .build();

        Section section = Section.builder()
                .id(1L)
                .course(course)
                .build();

        Quiz quiz = Quiz.builder()
                .id(1L)
                .title("Quiz")
                .description("description")
                .section(section)
                .link("link")
                .build();

        QuizDto quizDto = QuizDto.builder()
                .id(1L)
                .title("Quiz")
                .description("description")
                .link("link")
                .build();

        when(sectionRepository.findById(anyLong())).thenReturn(Optional.of(section));
        when(mapper.map(any(), eq(Quiz.class))).thenReturn(quiz);
        when(quizRepository.save(any())).thenReturn(quiz);
        when(mapper.map(any(), eq(QuizDto.class))).thenReturn(quizDto);

        ResponseEntity<Object> responseEntity = quizService.createNewQuiz(1L, quizDto);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Quiz", ((QuizDto) apiResponse.getData()).getTitle());

    }

    @Test
    void createNewQuiz_SectionEmpty() {
        QuizDto quizDto = QuizDto.builder()
                .id(1L)
                .title("Quiz")
                .description("description")
                .link("link")
                .build();

        when(sectionRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = quizService.createNewQuiz(1L, quizDto);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

    @Test
    void createNewVideo_Error() {
        QuizDto quizDto = QuizDto.builder()
                .id(1L)
                .title("Quiz")
                .description("description")
                .link("link")
                .build();

        when(sectionRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = quizService.createNewQuiz(1L, quizDto);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(AppConstant.ResponseCode.UNKNOWN_ERROR.getCode(), apiResponse.getStatus().getCode());
    }

    @Test
    void getQuizById_Success() {
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


        Quiz quiz = Quiz.builder()
                .id(1L)
                .title("Quiz")
                .description("description")
                .section(section)
                .link("link")
                .build();

        Boolean order = true;

        Boolean isCompleted = true;

        QuizDto quizDto = QuizDto.builder()
                .id(1L)
                .title("Quiz")
                .description("description")
                .link("link")
                .build();



        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(quizRepository.findById(anyLong())).thenReturn(Optional.of(quiz));
        when(orderRepository.existsByCourseIdAndUserId(anyLong(),anyLong())).thenReturn(order);
        when(mapper.map(any(), eq(QuizDto.class))).thenReturn(quizDto);
        when(quizCompletedRepository.existsByUserIdAndQuizId(anyLong(),anyLong())).thenReturn(isCompleted);


        ResponseEntity responseEntity = quizService.getQuizById(1L,"email");
        ApiResponse apiResponse = ((ApiResponse) responseEntity.getBody());


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Quiz", ((QuizDto) apiResponse.getData()).getTitle());
    }

    @Test
    void getQuizById_UserEmpty() {
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());

        ResponseEntity responseEntity = quizService.getQuizById(1L,"email");

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

    @Test
    void getQuizById_QuizEmpty() {
        User user = User.builder()
                .id(1L)
                .email("ilham@gmail.com")
                .build();

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));

        when(quizRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity responseEntity = quizService.getQuizById(1L,"email");

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

    @Test
    void getQuizById_NotEnroll() {
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


        Quiz quiz = Quiz.builder()
                .id(1L)
                .title("Quiz")
                .description("description")
                .section(section)
                .link("link")
                .build();

        Boolean order = false;


        QuizDto quizDto = QuizDto.builder()
                .id(1L)
                .title("Quiz")
                .description("description")
                .link("link")
                .build();


        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(quizRepository.findById(anyLong())).thenReturn(Optional.of(quiz));
        when(orderRepository.existsByCourseIdAndUserId(anyLong(),anyLong())).thenReturn(order);
        when(mapper.map(any(), eq(QuizDto.class))).thenReturn(quizDto);


        ResponseEntity responseEntity = quizService.getQuizById(1L,"email");
        ApiResponse apiResponse = ((ApiResponse) responseEntity.getBody());

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    void getVideoById_Error() {
        when(userRepository.findUserByEmail(anyString())).thenThrow(NullPointerException.class);
        ResponseEntity responseEntity = quizService.getQuizById(1L,"email");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

    }

    @Test
    void updateById_Success() {
        Section section = Section.builder()
                .id(1L)
                .build();

        Quiz quiz = Quiz.builder()
                .id(1L)
                .title("Quiz")
                .section(section)
                .build();

        QuizDto quizDto = QuizDto.builder()
                .id(1L)
                .title("Quiz")
                .build();

        when(quizRepository.findById(anyLong())).thenReturn(Optional.of(quiz));
        when(sectionRepository.findById(anyLong())).thenReturn(Optional.of(section));
        when(mapper.map(any(),eq(Quiz.class))).thenReturn(quiz);
        when(mapper.map(any(),eq(QuizDto.class))).thenReturn(quizDto);
        when(quizRepository.save(any())).thenReturn(quiz);

        ResponseEntity responseEntity = quizService.updateQuiz(1L,quizDto);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Quiz", ((QuizDto) apiResponse.getData()).getTitle());

    }

    @Test
    void updateById_QuizEmpty() {
        QuizDto quizDto = QuizDto.builder()
                .id(1L)
                .title("Quiz")
                .build();

        when(quizRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity responseEntity = quizService.updateQuiz(1L,quizDto);

        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        assertEquals(AppConstant.ResponseCode.DATA_NOT_FOUND.getCode(), apiResponse.getStatus().getCode());

    }

    @Test
    void updateById_Error() {
        QuizDto quizDto = QuizDto.builder()
                .id(1L)
                .title("Quiz")
                .build();

        when(quizRepository.findById(anyLong())).thenThrow(NullPointerException.class);

        ResponseEntity responseEntity = quizService.updateQuiz(1L,quizDto);

        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        assertEquals(AppConstant.ResponseCode.UNKNOWN_ERROR.getCode(), apiResponse.getStatus().getCode());

    }

    @Test
    void deleteById_Success() {
        when(quizRepository.findById(anyLong())).thenReturn(Optional.of(Quiz
                .builder()
                .id(1L)
                .build()));

        doNothing().when(quizRepository).delete(any());

        ResponseEntity<Object> responseEntity = quizService.deleteById(1L);
        verify(quizRepository, times(1)).delete(any());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void deleteById_QuizEmpty() {
        when(quizRepository.findById(anyLong())).thenReturn(Optional.empty());


        ResponseEntity<Object> responseEntity = quizService.deleteById(1L);

        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        assertEquals(AppConstant.ResponseCode.DATA_NOT_FOUND.getCode(), apiResponse.getStatus().getCode());
    }

    @Test
    void deleteById_Error() {
        when(quizRepository.findById(anyLong())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = quizService.deleteById(1L);

        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        assertEquals(AppConstant.ResponseCode.UNKNOWN_ERROR.getCode(), apiResponse.getStatus().getCode());
    }

}