package com.example.capstone.controller;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.capstone.domain.dto.SectionDto;
import com.example.capstone.service.SectionService;

import java.security.Principal;

@RestController
@RequestMapping("/")
@CrossOrigin
public class SectionController {
    @Autowired
    private SectionService sectionService;

    @GetMapping("/content")
    public ResponseEntity<Object> getAllContentBySectionId(@RequestParam(value = "sectionId") Long sectionId,
                                                           @RequestParam("page") int page,
                                                           @RequestParam("size") int size,
                                                           Principal principal){
        if (principal != null){
            return sectionService.getAllContentBySectionId(sectionId,page,size, principal.getName());
        } else {
            return ResponseUtil.build(AppConstant.ResponseCode.NOT_LOGGED_IN,null, HttpStatus.FORBIDDEN);
        }

    }

    @GetMapping("/sections")
    public ResponseEntity<Object> getAllSectionByCourseId(@RequestParam(value = "courseId") Long courseId,
                                                @RequestParam("page") int page,
                                                @RequestParam("size") int size){
        return sectionService.getAllSectionByCourseId(courseId,page,size);
    }

    @GetMapping("/section")
    public ResponseEntity<Object> getOneSection(@RequestParam(value = "id") Long id) {
        return sectionService.getSectionById(id);
    }

    @PostMapping("/admin/section")
    public ResponseEntity<Object> createNewSection(@RequestParam(value = "courseId")Long courseId,
                                                    @RequestBody SectionDto request){
        return sectionService.createNewSection(courseId,request);
    }


    @DeleteMapping("/admin/section")
    public ResponseEntity<Object> deleteSection(@RequestParam(value = "id") Long id){
        return sectionService.deleteById(id);
    }

}
