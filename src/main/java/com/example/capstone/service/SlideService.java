package com.example.capstone.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.domain.dao.*;
import com.example.capstone.domain.dto.SectionDto;
import com.example.capstone.domain.dto.VideoDto;
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
import com.example.capstone.domain.dto.SlideDto;
import com.example.capstone.util.ResponseUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SlideService {
    @Autowired
    SectionRepository sectionRepository;
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private SlideRepository slideRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SlideCompletedRepository slideCompletedRepository;

    public ResponseEntity<Object> getSlideById(Long id, String email) {
        log.info("Executing get Slide with ID : {}", id);
        try {
            Optional<User> userOptional = userRepository.findUserByEmail(email);
            if (userOptional.isEmpty()){
                log.info("User with Email [{}] not found",email);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND,null,HttpStatus.BAD_REQUEST);
            }
            Optional<Slide> slide = slideRepository.findById(id);
            if (slide.isEmpty()) {
                log.info("Slide with ID [{}] not found", id);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND,null,HttpStatus.BAD_REQUEST);
            }

            Course course = slide.get().getSection().getCourse();
            if (orderRepository.existsByCourseIdAndUserId(course.getId(),userOptional.get().getId())){
                SlideDto request = mapper.map(slide, SlideDto.class);
                Boolean isCompleted = slideCompletedRepository.existsByUserIdAndSlideId(userOptional.get().getId(),id);
                request.setCompleted(isCompleted);
                log.info("Successfully retrieved Slide with ID : {}", id);
                return ResponseUtil.build(ResponseCode.SUCCESS,request,HttpStatus.OK);
            } else {
                return ResponseUtil.build(ResponseCode.NOT_ENROLL,null,HttpStatus.FORBIDDEN);
            }
        } catch (Exception e ) {
            log.error("An error occurred while trying to get Slide with ID : {}. Error : {}", id, e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<Object> createNewSlide(Long sectionId, SlideDto request){
        log.info("Executing add new slide");
        try {
            Optional<Section> section = sectionRepository.findById(sectionId);
            Slide slide = mapper.map(request, Slide.class);
            slide.setSection(section.get());
            slideRepository.save(slide);

            log.info("Successfully added new slide");
            return ResponseUtil.build(ResponseCode.SUCCESS, mapper.map(slide, SlideDto.class), HttpStatus.OK);
        } catch (Exception e){
            log.error("An Error occurred while trying to add new slide. Error:{}",e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> completeSlide(Long id, String email) {
        log.info("Executing to make Slide complete with ID : {}", id);
        try {
            Optional<User> userOptional = userRepository.findUserByEmail(email);
            if (userOptional.isEmpty()) {
                log.info("User with Email [{}] not found", email);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            Optional<Slide> optionalSlide = slideRepository.findById(id);
            if (optionalSlide.isEmpty()) {
                log.info("Slide with ID [{}] not found", id);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }

            Course course = optionalSlide.get().getSection().getCourse();

            if(!orderRepository.existsByCourseIdAndUserId(course.getId(),userOptional.get().getId())){
                return ResponseUtil.build(ResponseCode.NOT_ENROLL,null,HttpStatus.FORBIDDEN);
            }

            if(!slideCompletedRepository.existsByUserIdAndSlideId(userOptional.get().getId(), id)){
                SlideCompleted slideCompleted = new SlideCompleted();
                slideCompleted.setSlide(optionalSlide.get());
                slideCompleted.setUser(userOptional.get());
                slideCompletedRepository.save(slideCompleted);
                log.info("Successfully Complete Slide With Id {}", id);
                return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS,
                        "Success Completed Slide",
                        HttpStatus.OK);
            } else {
                return ResponseUtil.build(ResponseCode.ALREADY_COMPLETED,null,HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            log.error("An Error occurred while trying to completed slide. Error:{}",e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> updateSlide(Long id, SlideDto request){
        log.info("Executing update slide");
        try {
            Optional<Slide> optionalSlide = slideRepository.findById(id);
            if (optionalSlide.isEmpty()) {
                log.info("Slide with Id [{}] not found", id);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            Optional<Section> section = sectionRepository.findById(optionalSlide.get().getSection().getId());
            optionalSlide.ifPresent(slide -> {
                slide.setId(id);
                slide.setTitle(request.getTitle());
                slide.setDescription(request.getDescription());
                slide.setLink(request.getLink());
                slide.setSection(section.get());;
                slideRepository.save(slide);
            });
            log.info("Successfully update slide");
            return ResponseUtil.build(ResponseCode.SUCCESS, mapper.map(optionalSlide.get(), SlideDto.class), HttpStatus.OK);
        } catch (Exception e){
            log.error("An Error occurred while trying to updated slide. Error:{}",e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteById(Long id){
        log.info("Executing delete existing slide");
        try {
            Optional<Slide> optionalSlide = slideRepository.findById(id);
            if (optionalSlide.isEmpty()){
                log.info("Slide with Id : [{}] is not found",id);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            slideRepository.delete(optionalSlide.get());
            log.info("Successfully delete slide with ID : [{}]", id);
            return ResponseUtil.build(ResponseCode.SUCCESS, null, HttpStatus.OK);
        } catch (Exception e){
            log.info("An error occurred while trying to delete existing slide. Error : {}",e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
