package com.example.capstone.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    public ResponseEntity<Object> getAllSection() {
        log.info("Executing get all section");
        try {
            List<Section> sectionList = sectionRepository.findAll();
            List<SectionDto> sectionDtoList = new ArrayList<>();
            for (Section section: sectionList){
                sectionDtoList.add(mapper.map(section, SectionDto.class));
            }
            log.info("Successfully retrieved all section");
            return ResponseUtil.build(ResponseCode.SUCCESS, sectionDtoList, HttpStatus.OK);
        } catch (Exception e){
            log.error("An error occurred while trying to get all section. Error : {}", e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
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

    public ResponseEntity<Object> updateSection(Long sectionId, SectionDto request){
        log.info("Executing update section");
        try {
            Optional<Section> optionalSection = sectionRepository.findById(sectionId);
            if (optionalSection.isEmpty()) {
                log.info("Section with Id [{}] not found", sectionId);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            Optional<Course> course = courseRepository.findById(optionalSection.get().getCourse().getId());
            optionalSection.ifPresent(section -> {
                section.setId(sectionId);
                section.setTitle(request.getTitle());
                section.setCourse(course.get());
                sectionRepository.save(section);
            });
            log.info("Successfully update section");
            return ResponseUtil.build(ResponseCode.SUCCESS, mapper.map(optionalSection.get(), SectionDto.class), HttpStatus.OK);
        } catch (Exception e){
            log.error("An Error occurred while trying to updated section. Error:{}",e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteById(Long id){
        log.info("Executing delete existing section");
        try {
            Optional<Section> optionalSection = sectionRepository.findById(id);
            if (optionalSection.isEmpty()){
                log.info("Section with Id : [{}] is not found",id);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND,null,HttpStatus.BAD_REQUEST);
            }
            sectionRepository.delete(optionalSection.get());
            log.info("Successfully delete section with ID : [{}]", id);
            return ResponseUtil.build(ResponseCode.SUCCESS,null,HttpStatus.OK);
        } catch (Exception e){
            log.info("An error occurred while trying to delete existing section. Error : {}",e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
