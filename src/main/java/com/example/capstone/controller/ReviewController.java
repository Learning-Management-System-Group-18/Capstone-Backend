package com.example.capstone.controller;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.domain.dto.ReviewDto;
import com.example.capstone.domain.payload.request.SearchRequest;
import com.example.capstone.service.ReviewService;
import com.example.capstone.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin
@RequestMapping()
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/review/search")
    public ResponseEntity<Object> searchReview(@RequestBody SearchRequest request){
        return reviewService.searchReview(request);
    }

    @GetMapping("/review")
    public ResponseEntity<Object> getReviewByCourse(@RequestParam("courseId") Long courseId,
                                                    @RequestParam(value = "rating",required = false) Integer rating ,
                                                    @RequestParam("page") int page,
                                                    @RequestParam("size") int limit) {
        return reviewService.getReviewByCourseId(courseId, rating, page, limit);
    }

    @PostMapping("/auth/review")
    public ResponseEntity<Object> addReview (@RequestBody ReviewDto request,
                                             @RequestParam("courseId") Long courseId,
                                             Principal principal) {
        if (principal != null){
            return reviewService.createReview(request, principal.getName(), courseId);
        } else {
            return ResponseUtil.build(AppConstant.ResponseCode.NOT_LOGGED_IN,null, HttpStatus.FORBIDDEN);
        }
    }
}
