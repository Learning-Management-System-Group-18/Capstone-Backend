package com.example.capstone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.capstone.domain.dto.SlideDto;
import com.example.capstone.service.SlideService;

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

    @GetMapping("/slide")
    public ResponseEntity<Object> getOneSlide(@RequestParam(value = "id") Long id) {
        return slideService.getSlideById(id);
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
