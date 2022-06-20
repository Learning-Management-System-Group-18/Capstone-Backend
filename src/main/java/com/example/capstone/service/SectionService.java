package com.example.capstone.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.example.capstone.domain.dto.CourseDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.capstone.constant.AppConstant.*;
import com.example.capstone.domain.dao.Course;
import com.example.capstone.domain.dao.Section;
import com.example.capstone.domain.dto.SectionDto;
import com.example.capstone.repository.CourseRepository;
import com.example.capstone.repository.SectionRepository;
import com.example.capstone.util.ResponseUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SectionService {
    
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private SectionRepository sectionRepository;

    public ResponseEntity<Object> getAllSectionByCourseId(Long courseId, int page, int size) {
        log.info("Executing get all section");
        try {
            Pageable pageable = PageRequest.of(page-1,size);
            Page<Section> sections = sectionRepository.findAllByCourseId(courseId,pageable);

            List<SectionDto> request = new ArrayList<>();
            for (Section section: sections){
                SectionDto sectionDto = mapper.map(section, SectionDto.class);
                request.add(sectionDto);
            }
            log.info("Successfully retrieved all section");
            return ResponseUtil.build(ResponseCode.SUCCESS, request, HttpStatus.OK);
        } catch (Exception e){
            log.error("An error occurred while trying to get all section. Error : {}", e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getSectionById(Long id) {
        log.info("Executing get Section with ID : {}", id);
        try {
            Optional<Section> section = sectionRepository.findById(id);
            if (section.isEmpty()) {
                log.info("Section with ID [{}] not found", id);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND,null,HttpStatus.BAD_REQUEST);
            }

            SectionDto request = mapper.map(section, SectionDto.class);
            log.info("Successfully retrieved Section with ID : {}", id);
            return ResponseUtil.build(ResponseCode.SUCCESS,request,HttpStatus.OK);
        } catch (Exception e ) {
            log.error("An error occurred while trying to get Section with ID : {}. Error : {}", id, e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<Object> createNewSection(Long courseId, SectionDto request){
        log.info("Executing add new section");
        try {
            Optional<Course> course = courseRepository.findById(courseId);
            Section section = mapper.map(request, Section.class);
            section.setCourse(course.get());
            sectionRepository.save(section);

            log.info("Successfully added new section");
            return ResponseUtil.build(ResponseCode.SUCCESS, mapper.map(section, SectionDto.class), HttpStatus.OK);
        } catch (Exception e){
            log.error("An Error occurred while trying to add new section. Error:{}",e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
