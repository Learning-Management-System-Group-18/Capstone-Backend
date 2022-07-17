package com.example.capstone.controller;

import com.example.capstone.domain.dto.CourseDto;
import com.example.capstone.domain.dto.MentorDto;
import com.example.capstone.domain.dto.ToolDto;
import com.example.capstone.service.MentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/")
@CrossOrigin
public class MentorController {
    @Autowired
    MentorService mentorService;

    @GetMapping("/mentors")
    public ResponseEntity<Object> getAllMentorByCourseId(@RequestParam(value = "courseId")Long courseId) {
        return mentorService.getAllMentorByCourseId(courseId);
    }

    @GetMapping("/mentor/top")
    public ResponseEntity<Object> topMentor(){
        return mentorService.topMentor();
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

    @PutMapping(
            path = "/admin/mentor",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateTool(@RequestParam(value = "id") Long id,
                                             @ModelAttribute MentorDto request,
                                             @RequestParam(value = "image", required = false) MultipartFile file ){
        return mentorService.updateById(id,request,file);
    }
}
