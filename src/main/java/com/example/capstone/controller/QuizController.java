package com.example.capstone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping("/quiz")
    public ResponseEntity<Object> getAllQuiz(){
        return quizService.getAllQuiz();
    }

    @PostMapping("/admin/quiz")
    public ResponseEntity<Object> createNewQuiz(@RequestParam(value = "sectionId")Long sectionId,
                                                    @RequestBody QuizDto request){
        return quizService.createNewQuiz(sectionId, request);
    }

    @PutMapping("/admin/quiz")
    public ResponseEntity<Object> updateQuiz(@RequestParam(value = "quizId") Long quizId,
                                                @RequestBody QuizDto request){
        return quizService.updateQuiz(quizId, request);
    }

    @DeleteMapping("/admin/quiz")
    public ResponseEntity<Object> deleteQuiz(@RequestParam(value = "id") Long id){
        return quizService.deleteById(id);
    }
}
