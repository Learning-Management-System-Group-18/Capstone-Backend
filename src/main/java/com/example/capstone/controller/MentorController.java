package com.example.capstone.controller;

import com.example.capstone.domain.dto.CourseDto;
import com.example.capstone.domain.dto.MentorDto;
import com.example.capstone.service.MentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/")
public class MentorController {
    @Autowired
    MentorService mentorService;

    @GetMapping("/mentors")
    public ResponseEntity<Object> getAllMentorByCourseId(@RequestParam(value = "courseId")Long courseId) {
        return mentorService.getAllMentorByCourseId(courseId);
    }

    @PostMapping(
            path = "/admin/mentor",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createNewMentor(@RequestParam(value = "courseId") Long courseId,
                                                  @ModelAttribute MentorDto request,
                                                  @RequestPart(value = "image") MultipartFile file){
        return mentorService.createNewMentor(courseId,request,file);
    }
}
