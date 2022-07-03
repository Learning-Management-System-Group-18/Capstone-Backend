package com.example.capstone.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.domain.dao.*;
import com.example.capstone.domain.dto.UserProfileDto;
import com.example.capstone.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.capstone.constant.AppConstant.ResponseCode;
import com.example.capstone.domain.dto.VideoDto;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private VideoCompletedRepository videoCompletedRepository;

    public ResponseEntity<Object> getAllVideoBySectionId(Long sectionId, int page, int size) {
        log.info("Executing get all video");
        try {
            Pageable pageable = PageRequest.of(page-1,size);
            Page<Video> videos = videoRepository.findAllBySectionId(sectionId,pageable);

            List<VideoDto> request = new ArrayList<>();
            for (Video video: videos){
                VideoDto videoDto = mapper.map(video, VideoDto.class);
                request.add(videoDto);
            }
            log.info("Successfully retrieved all video");
            return ResponseUtil.build(ResponseCode.SUCCESS, request, HttpStatus.OK);
        } catch (Exception e){
            log.error("An error occurred while trying to get all video. Error : {}", e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getVideoById(Long id, String email) {
        log.info("Executing get Video with ID : {}", id);
        try {
            Optional<User> userOptional = userRepository.findUserByEmail(email);
            if (userOptional.isEmpty()){
                log.info("User with Email [{}] not found",email);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND,null,HttpStatus.BAD_REQUEST);
            }
            Optional<Video> video = videoRepository.findById(id);
            if (video.isEmpty()) {
                log.info("Video with ID [{}] not found", id);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND,null,HttpStatus.BAD_REQUEST);
            }

            Course course = video.get().getSection().getCourse();
            if (orderRepository.existsByCourseIdAndUserId(course.getId(),userOptional.get().getId())){
                VideoDto request = mapper.map(video, VideoDto.class);
                Boolean isCompleted = videoCompletedRepository.existsByUserIdAndVideoId(userOptional.get().getId(),id);
                request.setCompleted(isCompleted);
                log.info("Successfully retrieved Video with ID : {}", id);
                return ResponseUtil.build(ResponseCode.SUCCESS,request,HttpStatus.OK);
            } else {
                return ResponseUtil.build(ResponseCode.NOT_ENROLL,null,HttpStatus.FORBIDDEN);
            }
        } catch (Exception e ) {
            log.error("An error occurred while trying to get Video with ID : {}. Error : {}", id, e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
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

    public ResponseEntity<Object> completeVideo(Long id, String email) {
        log.info("Executing to make Video complete with ID : {}", id);
        try {
            Optional<User> userOptional = userRepository.findUserByEmail(email);
            if (userOptional.isEmpty()) {
                log.info("User with Email [{}] not found", email);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            Optional<Video> optionalVideo = videoRepository.findById(id);
            if (optionalVideo.isEmpty()) {
                log.info("Video with ID [{}] not found", id);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }

            Course course = optionalVideo.get().getSection().getCourse();

            if(!orderRepository.existsByCourseIdAndUserId(course.getId(),userOptional.get().getId())){
                return ResponseUtil.build(ResponseCode.NOT_ENROLL,null,HttpStatus.FORBIDDEN);
            }

            if(!videoCompletedRepository.existsByUserIdAndVideoId(userOptional.get().getId(), id)){
                VideoCompleted videoCompleted = new VideoCompleted();
                videoCompleted.setVideo(optionalVideo.get());
                videoCompleted.setUser(userOptional.get());
                videoCompletedRepository.save(videoCompleted);
                log.info("Successfully Complete Video With Id {}", id);
                return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS,
                        "Success Completed Video",
                        HttpStatus.OK);
            } else {
                return ResponseUtil.build(ResponseCode.ALREADY_COMPLETED,null,HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            log.error("An Error occurred while trying to completed video. Error:{}",e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> updateVideo(Long id, VideoDto request){
        log.info("Executing update video");
        try {
            Optional<Video> optionalVideo = videoRepository.findById(id);
            if (optionalVideo.isEmpty()) {
                log.info("Video with Id [{}] not found", id);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            Optional<Section> section = sectionRepository.findById(optionalVideo.get().getSection().getId());
            optionalVideo.ifPresent(video -> {
                video.setId(id);
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
