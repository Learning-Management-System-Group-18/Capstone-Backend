package com.example.capstone.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.domain.dao.*;
import com.example.capstone.domain.dto.SlideDto;
import com.example.capstone.domain.dto.VideoDto;
import com.example.capstone.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.capstone.constant.AppConstant.ResponseCode;
import com.example.capstone.domain.dto.QuizDto;
import com.example.capstone.util.ResponseUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QuizService {
    @Autowired
    SectionRepository sectionRepository;
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private QuizCompletedRepository quizCompletedRepository;

    public ResponseEntity<Object> getQuizById(Long id,String email) {
        log.info("Executing get Quiz with ID : {}", id);
        try {
            Optional<User> userOptional = userRepository.findUserByEmail(email);
            if (userOptional.isEmpty()){
                log.info("User with Email [{}] not found",email);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND,null,HttpStatus.BAD_REQUEST);
            }
            Optional<Quiz> quiz = quizRepository.findById(id);
            if (quiz.isEmpty()) {
                log.info("Quiz with ID [{}] not found", id);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND,null,HttpStatus.BAD_REQUEST);
            }

            Course course = quiz.get().getSection().getCourse();
            if (orderRepository.existsByCourseIdAndUserId(course.getId(),userOptional.get().getId())){
                QuizDto request = mapper.map(quiz, QuizDto.class);
                Boolean isCompleted = quizCompletedRepository.existsByUserIdAndQuizId(userOptional.get().getId(),id);
                request.setCompleted(isCompleted);
                log.info("Successfully retrieved Quiz with ID : {}", id);
                return ResponseUtil.build(ResponseCode.SUCCESS,request,HttpStatus.OK);
            } else {
                return ResponseUtil.build(ResponseCode.NOT_ENROLL,null,HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            log.error("An error occurred while trying to get Quiz with ID : {}. Error : {}", id, e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> completeQuiz(Long id, String email) {
        log.info("Executing to make Quiz complete with ID : {}", id);
        try {
            Optional<User> userOptional = userRepository.findUserByEmail(email);
            if (userOptional.isEmpty()) {
                log.info("User with Email [{}] not found", email);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            Optional<Quiz> optionalQuiz = quizRepository.findById(id);
            if (optionalQuiz.isEmpty()) {
                log.info("Quiz with ID [{}] not found", id);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }

            Course course = optionalQuiz.get().getSection().getCourse();

            if(!orderRepository.existsByCourseIdAndUserId(course.getId(),userOptional.get().getId())){
                return ResponseUtil.build(ResponseCode.NOT_ENROLL,null,HttpStatus.FORBIDDEN);
            }

            if(!quizCompletedRepository.existsByUserIdAndQuizId(userOptional.get().getId(), id)){
                QuizCompleted quizCompleted = new QuizCompleted();
                quizCompleted.setQuiz(optionalQuiz.get());
                quizCompleted.setUser(userOptional.get());
                quizCompletedRepository.save(quizCompleted);
                log.info("Successfully Complete Quiz With Id {}", id);
                return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS,
                        "Success Completed Quiz",
                        HttpStatus.OK);
            } else {
                return ResponseUtil.build(ResponseCode.ALREADY_COMPLETED,null,HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            log.error("An Error occurred while trying to completed quiz. Error:{}",e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> createNewQuiz(Long sectionId, QuizDto request){
        log.info("Executing add new quiz");
        try {
            Optional<Section> section = sectionRepository.findById(sectionId);
            Quiz quiz = mapper.map(request, Quiz.class);
            quiz.setSection(section.get());
            quizRepository.save(quiz);

            log.info("Successfully added new quiz");
            return ResponseUtil.build(ResponseCode.SUCCESS, mapper.map(quiz, QuizDto.class), HttpStatus.OK);
        } catch (Exception e){
            log.error("An Error occurred while trying to add new quiz. Error:{}",e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> updateQuiz(Long id, QuizDto request){
        log.info("Executing update quiz");
        try {
            Optional<Quiz> optionalQuiz = quizRepository.findById(id);
            if (optionalQuiz.isEmpty()) {
                log.info("Quiz with Id [{}] not found", id);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            Optional<Section> section = sectionRepository.findById(optionalQuiz.get().getSection().getId());
            optionalQuiz.ifPresent(quiz -> {
                quiz.setId(id);
                quiz.setTitle(request.getTitle());
                quiz.setDescription(request.getDescription());
                quiz.setLink(request.getLink());
                quiz.setSection(section.get());;
                quizRepository.save(quiz);
            });
            log.info("Successfully update quiz");
            return ResponseUtil.build(ResponseCode.SUCCESS, mapper.map(optionalQuiz.get(), QuizDto.class), HttpStatus.OK);
        } catch (Exception e){
            log.error("An Error occurred while trying to updated quiz. Error:{}",e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteById(Long id){
        log.info("Executing delete existing quiz");
        try {
            Optional<Quiz> optionalQuiz = quizRepository.findById(id);
            if (optionalQuiz.isEmpty()){
                log.info("Quiz with Id : [{}] is not found",id);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            quizRepository.delete(optionalQuiz.get());
            log.info("Successfully delete quiz with ID : [{}]", id);
            return ResponseUtil.build(ResponseCode.SUCCESS, null, HttpStatus.OK);
        } catch (Exception e){
            log.info("An error occurred while trying to delete existing quiz. Error : {}",e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
