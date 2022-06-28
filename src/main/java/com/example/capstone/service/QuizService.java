package com.example.capstone.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.capstone.domain.dao.Slide;
import com.example.capstone.domain.dto.SlideDto;
import com.example.capstone.domain.dto.VideoDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.capstone.constant.AppConstant.ResponseCode;
import com.example.capstone.domain.dao.Quiz;
import com.example.capstone.domain.dao.Section;
import com.example.capstone.domain.dto.QuizDto;
import com.example.capstone.repository.QuizRepository;
import com.example.capstone.repository.SectionRepository;
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

    public ResponseEntity<Object> getAllQuizBySectionId(Long sectionId, int page, int size) {
        log.info("Executing get all quiz");
        try {
            Pageable pageable = PageRequest.of(page-1,size);
            Page<Quiz> quizzes = quizRepository.findAllBySectionId(sectionId,pageable);

            List<QuizDto> request = new ArrayList<>();
            for (Quiz quiz: quizzes){
                QuizDto quizDto = mapper.map(quiz, QuizDto.class);
                request.add(quizDto);
            }
            log.info("Successfully retrieved all quiz");
            return ResponseUtil.build(ResponseCode.SUCCESS, request, HttpStatus.OK);
        } catch (Exception e){
            log.error("An error occurred while trying to get all quiz. Error : {}", e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getQuizById(Long id) {
        log.info("Executing get Quiz with ID : {}", id);
        try {
            Optional<Quiz> quiz = quizRepository.findById(id);
            if (quiz.isEmpty()) {
                log.info("Quiz with ID [{}] not found", id);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }

            QuizDto request = mapper.map(quiz, QuizDto.class);
            log.info("Successfully retrieved Quiz with ID : {}", id);
            return ResponseUtil.build(ResponseCode.SUCCESS, request, HttpStatus.OK);
        } catch (Exception e) {
            log.error("An error occurred while trying to get Quiz with ID : {}. Error : {}", id, e.getMessage());
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
}
