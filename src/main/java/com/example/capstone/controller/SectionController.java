package com.example.capstone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.capstone.domain.dto.SectionDto;
import com.example.capstone.service.SectionService;

@RestController
@RequestMapping("/")
@CrossOrigin
public class SectionController {
    @Autowired
    private SectionService sectionService;

    @GetMapping("/content")
    public ResponseEntity<Object> getAllContentBySectionId(@RequestParam(value = "sectionId") Long sectionId,
                                                          @RequestParam("page") int page,
                                                          @RequestParam("size") int size){
        return sectionService.getAllContentBySectionId(sectionId,page,size);
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
