package com.example.capstone.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.capstone.domain.dao.Video;
import com.example.capstone.domain.dto.SectionDto;
import com.example.capstone.domain.dto.VideoDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.capstone.constant.AppConstant.ResponseCode;
import com.example.capstone.domain.dao.Section;
import com.example.capstone.domain.dao.Slide;
import com.example.capstone.domain.dto.SlideDto;
import com.example.capstone.repository.SectionRepository;
import com.example.capstone.repository.SlideRepository;
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

    public ResponseEntity<Object> getAllSlideBySectionId(Long sectionId, int page, int size) {
        log.info("Executing get all slide");
        try {
            Pageable pageable = PageRequest.of(page-1,size);
            Page<Slide> slides = slideRepository.findAllBySectionId(sectionId,pageable);

            List<SlideDto> request = new ArrayList<>();
            for (Slide slide: slides){
                SlideDto slideDto = mapper.map(slide, SlideDto.class);
                request.add(slideDto);
            }
            log.info("Successfully retrieved all slide");
            return ResponseUtil.build(ResponseCode.SUCCESS, request, HttpStatus.OK);
        } catch (Exception e){
            log.error("An error occurred while trying to get all slide. Error : {}", e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getSlideById(Long id) {
        log.info("Executing get Slide with ID : {}", id);
        try {
            Optional<Slide> slide = slideRepository.findById(id);
            if (slide.isEmpty()) {
                log.info("Slide with ID [{}] not found", id);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND,null,HttpStatus.BAD_REQUEST);
            }

            SlideDto request = mapper.map(slide, SlideDto.class);
            log.info("Successfully retrieved Slide with ID : {}", id);
            return ResponseUtil.build(ResponseCode.SUCCESS,request,HttpStatus.OK);
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
}
