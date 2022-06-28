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

    public ResponseEntity<Object> updateQuiz(Long quizId, QuizDto request){
        log.info("Executing update quiz");
        try {
            Optional<Quiz> optionalQuiz = quizRepository.findById(quizId);
            if (optionalQuiz.isEmpty()) {
                log.info("Quiz with Id [{}] not found", quizId);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            Optional<Section> section = sectionRepository.findById(optionalQuiz.get().getSection().getId());
            optionalQuiz.ifPresent(quiz -> {
                quiz.setId(quizId);
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
