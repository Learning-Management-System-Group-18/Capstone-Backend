package com.example.capstone.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.capstone.constant.AppConstant.ResponseCode;
import com.example.capstone.domain.dao.Section;
import com.example.capstone.domain.dao.Video;
import com.example.capstone.domain.dto.VideoDto;
import com.example.capstone.repository.SectionRepository;
import com.example.capstone.repository.VideoRepository;
import com.example.capstone.util.ResponseUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VideoService {
    
    @Autowired
    SectionRepository sectionRepository;
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private VideoRepository videoRepository;

    public ResponseEntity<Object> getAllVideo() {
        log.info("Executing get all video");
        try {
            List<Video> videoList = videoRepository.findAll();
            List<VideoDto> videoDtoList = new ArrayList<>();
            for (Video video: videoList){
                videoDtoList.add(mapper.map(video, VideoDto.class));
            }
            log.info("Successfully retrieved all video");
            return ResponseUtil.build(ResponseCode.SUCCESS, videoDtoList, HttpStatus.OK);
        } catch (Exception e){
            log.error("An error occurred while trying to get all video. Error : {}", e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> createNewVideo(Long sectionId, VideoDto request){
        log.info("Executing add new video");
        try {
            Optional<Section> section = sectionRepository.findById(sectionId);
            Video video = mapper.map(request, Video.class);
            video.setSection(section.get());
            videoRepository.save(video);

            log.info("Successfully added new video");
            return ResponseUtil.build(ResponseCode.SUCCESS, mapper.map(video, VideoDto.class), HttpStatus.OK);
        } catch (Exception e){
            log.error("An Error occurred while trying to add new video. Error:{}",e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<Object> updateVideo(Long videoId, VideoDto request){
        log.info("Executing update video");
        try {
            Optional<Video> optionalVideo = videoRepository.findById(videoId);
            if (optionalVideo.isEmpty()) {
                log.info("Video with Id [{}] not found", videoId);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            Optional<Section> section = sectionRepository.findById(optionalVideo.get().getSection().getId());
            optionalVideo.ifPresent(video -> {
                video.setId(videoId);
                video.setTitle(request.getTitle());
                video.setDescription(request.getDescription());
                video.setLink(request.getLink());
                video.setSection(section.get());;
                videoRepository.save(video);
            });
            log.info("Successfully update video");
            return ResponseUtil.build(ResponseCode.SUCCESS, mapper.map(optionalVideo.get(), VideoDto.class), HttpStatus.OK);
        } catch (Exception e){
            log.error("An Error occurred while trying to updated video. Error:{}",e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteById(Long id){
        log.info("Executing delete existing video");
        try {
            Optional<Video> optionalVideo = videoRepository.findById(id);
            if (optionalVideo.isEmpty()){
                log.info("Video with Id : [{}] is not found",id);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND,null,HttpStatus.BAD_REQUEST);
            }
            videoRepository.delete(optionalVideo.get());
            log.info("Successfully delete video with ID : [{}]", id);
            return ResponseUtil.build(ResponseCode.SUCCESS,null,HttpStatus.OK);
        } catch (Exception e){
            log.info("An error occurred while trying to delete existing video. Error : {}",e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}