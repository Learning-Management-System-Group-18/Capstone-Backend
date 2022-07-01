package com.example.capstone.controller;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.capstone.domain.dto.SlideDto;
import com.example.capstone.service.SlideService;

import java.security.Principal;

@RestController
@RequestMapping("/")
@CrossOrigin
public class SlideController {
    @Autowired
    private SlideService slideService;

    @GetMapping("/slides")
    public ResponseEntity<Object> getAllSlideBySectionId(@RequestParam(value = "sectionId") Long sectionId,
                                                         @RequestParam("page") int page,
                                                         @RequestParam("size") int size){
        return slideService.getAllSlideBySectionId(sectionId,page,size);
    }

    @GetMapping("/auth/slide")
    public ResponseEntity<Object> getOneSlide(@RequestParam(value = "id") Long id,
                                              Principal principal) {
        if (principal != null){
            return slideService.getSlideById(id, principal.getName());
        } else {
            return ResponseUtil.build(AppConstant.ResponseCode.NOT_LOGGED_IN,null, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/admin/slide")
    public ResponseEntity<Object> createNewSlide(@RequestParam(value = "sectionId")Long sectionId,
                                                    @RequestBody SlideDto request){
        return slideService.createNewSlide(sectionId,request);
    }

    @PutMapping("/admin/slide")
    public ResponseEntity<Object> updateSlide(@RequestParam(value = "id") Long id,
                                              @RequestBody SlideDto request){
        return slideService.updateSlide(id, request);
    }

    @DeleteMapping("/admin/slide")
    public ResponseEntity<Object> deleteSlide(@RequestParam(value = "id") Long id){
        return slideService.deleteById(id);
    }
}
