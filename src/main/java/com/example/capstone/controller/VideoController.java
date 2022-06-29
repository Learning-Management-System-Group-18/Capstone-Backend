package com.example.capstone.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.capstone.domain.dto.VideoDto;
import com.example.capstone.service.VideoService;

@RestController
@RequestMapping("/")
@CrossOrigin
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

    @PutMapping("/admin/video")
    public ResponseEntity<Object> updateVideo(@RequestParam(value = "id") Long id,
                                              @RequestBody VideoDto request){
        return videoService.updateVideo(id, request);
    }

    @DeleteMapping("/admin/video")
    public ResponseEntity<Object> deleteVideo(@RequestParam(value = "id") Long id){
        return videoService.deleteById(id);
    }
}
