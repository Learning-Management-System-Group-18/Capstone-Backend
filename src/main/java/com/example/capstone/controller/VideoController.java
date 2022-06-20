package com.example.capstone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.capstone.domain.dto.VideoDto;
import com.example.capstone.service.VideoService;

@RestController
@RequestMapping("/")
public class VideoController {
    @Autowired
    private VideoService videoService;

    @GetMapping("/videos")
    public ResponseEntity<Object> getAllVideoBySectionId(@RequestParam(value = "sectionId") Long sectionId,
                                                          @RequestParam("page") int page,
                                                          @RequestParam("size") int size){
        return videoService.getAllVideoBySectionId(sectionId,page,size);
    }

    @GetMapping("/video")
    public ResponseEntity<Object> getOneVideo(@RequestParam(value = "id") Long id) {
        return videoService.getVideoById(id);
    }

    @PostMapping("/admin/video")
    public ResponseEntity<Object> createNewVideo(@RequestParam(value = "sectionId")Long sectionId,
                                                    @RequestBody VideoDto request){
        return videoService.createNewVideo(sectionId,request);
    }
}
