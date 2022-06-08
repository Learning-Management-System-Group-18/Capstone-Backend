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

    public ResponseEntity<Object> getAllSlide() {
        log.info("Executing get all slide");
        try {
            List<Slide> slideList = slideRepository.findAll();
            List<SlideDto> slideDtoList = new ArrayList<>();
            for (Slide slide: slideList){
                slideDtoList.add(mapper.map(slide, SlideDto.class));
            }
            log.info("Successfully retrieved all slide");
            return ResponseUtil.build(ResponseCode.SUCCESS, slideDtoList, HttpStatus.OK);
        } catch (Exception e){
            log.error("An error occurred while trying to get all slide. Error : {}", e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
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
