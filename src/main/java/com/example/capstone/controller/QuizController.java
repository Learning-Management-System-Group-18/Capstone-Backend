package com.example.capstone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.capstone.domain.dto.QuizDto;
import com.example.capstone.service.QuizService;

@RestController
@RequestMapping("/")
public class QuizController {
    @Autowired
    private QuizService quizService;

    @GetMapping("/quizzes")
    public ResponseEntity<Object> getAllQuizBySectionId(@RequestParam(value = "sectionId") Long sectionId,
                                                         @RequestParam("page") int page,
                                                         @RequestParam("size") int size){
        return quizService.getAllQuizBySectionId(sectionId,page,size);
    }

    @GetMapping("/quiz")
    public ResponseEntity<Object> getOneQuiz(@RequestParam(value = "id") Long id) {
        return quizService.getQuizById(id);
    }

    @PostMapping("/admin/quiz")
    public ResponseEntity<Object> createNewQuiz(@RequestParam(value = "sectionId")Long sectionId,
                                                    @RequestBody QuizDto request){
        return quizService.createNewQuiz(sectionId, request);
    }
}
