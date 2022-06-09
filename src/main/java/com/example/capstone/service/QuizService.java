package com.example.capstone.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    public ResponseEntity<Object> getAllQuiz() {
        log.info("Executing get all quiz");
        try {
            List<Quiz> quizList = quizRepository.findAll();
            List<QuizDto> quizDtoList = new ArrayList<>();
            for (Quiz quiz: quizList){
                quizDtoList.add(mapper.map(quiz, QuizDto.class));
            }
            log.info("Successfully retrieved all quiz");
            return ResponseUtil.build(ResponseCode.SUCCESS, quizDtoList, HttpStatus.OK);
        } catch (Exception e){
            log.error("An error occurred while trying to get all quiz. Error : {}", e.getMessage());
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
