package com.example.capstone.controller;

import com.example.capstone.domain.dto.MentorDto;
import com.example.capstone.domain.dto.ToolDto;
import com.example.capstone.service.MentorService;
import com.example.capstone.service.ToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/")
public class ToolController {
    @Autowired
    ToolService toolService;

    @GetMapping("/tools")
    public ResponseEntity<Object> getAllToolByCourseId(@RequestParam(value = "courseId")Long courseId) {
        return toolService.getAllToolByCourseId(courseId);
    }

    @PostMapping(
            path = "/admin/tool",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createNewTool(@RequestParam(value = "courseId") Long courseId,
                                                  @ModelAttribute ToolDto request,
                                                  @RequestPart(value = "image") MultipartFile file){
        return toolService.createNewTool(courseId,request,file);
    }
}
