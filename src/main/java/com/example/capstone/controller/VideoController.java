package com.example.capstone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping("/video")
    public ResponseEntity<Object> getAllVideo(){
        return videoService.getAllVideo();
    }

    @PostMapping("/admin/video")
    public ResponseEntity<Object> createNewVideo(@RequestParam(value = "sectionId")Long sectionId,
                                                    @RequestBody VideoDto request){
        return videoService.createNewVideo(sectionId,request);
    }
    @PutMapping("/admin/video")
    public ResponseEntity<Object> updateVideo(@RequestParam(value = "videoId") Long videoId,
                                                @RequestBody VideoDto request){
        return videoService.updateVideo(videoId, request);
    }

    @DeleteMapping("/admin/video")
    public ResponseEntity<Object> deleteVideo(@RequestParam(value = "id") Long id){
        return videoService.deleteById(id);
    }
}
