package com.example.capstone.controller;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.capstone.domain.dto.QuizDto;
import com.example.capstone.service.QuizService;

import java.security.Principal;

@RestController
@RequestMapping("/")
@CrossOrigin
public class QuizController {
    @Autowired
    private QuizService quizService;

    @GetMapping("/quizzes")
    public ResponseEntity<Object> getAllQuizBySectionId(@RequestParam(value = "sectionId") Long sectionId,
                                                         @RequestParam("page") int page,
                                                         @RequestParam("size") int size){
        return quizService.getAllQuizBySectionId(sectionId,page,size);
    }

    @GetMapping("/auth/quiz")
    public ResponseEntity<Object> getOneQuiz(@RequestParam(value = "id") Long id,
                                             Principal principal) {
        if (principal != null){
            return quizService.getQuizById(id, principal.getName());
        } else {
            return ResponseUtil.build(AppConstant.ResponseCode.NOT_LOGGED_IN,null, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/admin/quiz")
    public ResponseEntity<Object> createNewQuiz(@RequestParam(value = "sectionId")Long sectionId,
                                                    @RequestBody QuizDto request){
        return quizService.createNewQuiz(sectionId, request);
    }

    @PutMapping("/admin/quiz")
    public ResponseEntity<Object> updateQuiz(@RequestParam(value = "id") Long id,
                                             @RequestBody QuizDto request){
        return quizService.updateQuiz(id, request);
    }

    @DeleteMapping("/admin/quiz")
    public ResponseEntity<Object> deleteQuiz(@RequestParam(value = "id") Long id){
        return quizService.deleteById(id);
    }
}
