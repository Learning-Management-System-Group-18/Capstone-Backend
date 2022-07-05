package com.example.capstone.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.example.capstone.domain.dao.*;
import com.example.capstone.domain.dto.ContentDto;
import com.example.capstone.domain.dto.CourseDto;
import com.example.capstone.domain.payload.response.QuizResponse;
import com.example.capstone.domain.payload.response.SlideResponse;
import com.example.capstone.domain.payload.response.VideoResponse;
import com.example.capstone.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.capstone.constant.AppConstant.*;
import com.example.capstone.domain.dto.SectionDto;
import com.example.capstone.util.ResponseUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SectionService {
    
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private SlideRepository slideRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SlideCompletedRepository slideCompletedRepository;

    @Autowired
    private VideoCompletedRepository videoCompletedRepository;

    @Autowired
    private QuizCompletedRepository quizCompletedRepository;


    public ResponseEntity<Object> getAllContentBySectionId(Long sectionId, int page, int size, String email) {
        log.info("Executing get all content");
        try {

            Optional<User> userOptional = userRepository.findUserByEmail(email);
            if (userOptional.isEmpty()){
                log.info("User with Email [{}] not found",email);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND,null,HttpStatus.BAD_REQUEST);
            }

            Pageable pageable = PageRequest.of(page-1,size);
            Page<Slide> slides = slideRepository.findAllBySectionId(sectionId,pageable);
            Page<Video> videos = videoRepository.findAllBySectionId(sectionId,pageable);
            Page<Quiz> quizzes = quizRepository.findAllBySectionId(sectionId,pageable);

            ContentDto request = new ContentDto();
            List<VideoResponse> videoResponses = new ArrayList<>();
            List<QuizResponse> quizResponses = new ArrayList<>();
            List<SlideResponse> slideResponses = new ArrayList<>();

            for (Slide slide: slides){
                SlideResponse slideResponse = mapper.map(slide, SlideResponse.class);
                Boolean isCompleted = slideCompletedRepository.existsByUserIdAndSlideId(userOptional.get().getId(), slideResponse.getId());
                slideResponse.setCompleted(isCompleted);
                slideResponses.add(slideResponse);
            }

            for (Video video: videos){
                VideoResponse videoResponse = mapper.map(video, VideoResponse.class);
                Boolean isCompleted = videoCompletedRepository.existsByUserIdAndVideoId(userOptional.get().getId(), videoResponse.getId());
                videoResponse.setCompleted(isCompleted);
                videoResponses.add(videoResponse);
            }

            for (Quiz quiz: quizzes){
                QuizResponse quizResponse = mapper.map(quiz, QuizResponse.class);
                Boolean isCompleted = quizCompletedRepository.existsByUserIdAndQuizId(userOptional.get().getId(), quizResponse.getId());
                quizResponse.setCompleted(isCompleted);
                quizResponses.add(quizResponse);
            }
            request.setQuiz(quizResponses);
            request.setSlide(slideResponses);
            request.setVideo(videoResponses);

            log.info("Successfully retrieved all content");
            return ResponseUtil.build(ResponseCode.SUCCESS, request, HttpStatus.OK);
        } catch (Exception e){
            log.error("An error occurred while trying to get all content. Error : {}", e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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

    public ResponseEntity<Object> deleteById(Long id){
        log.info("Executing delete existing Section");
        try {
            Optional<Section> optionalSection = sectionRepository.findById(id);
            if (optionalSection.isEmpty()) {
                log.info("Section with ID : [{}] is not found", id);
                return ResponseUtil.build(ResponseCode.DATA_NOT_FOUND, null, HttpStatus.BAD_REQUEST);
            }
            sectionRepository.delete(optionalSection.get());
            log.info("Successfully deleted Section with ID : [{}]", id);
            return ResponseUtil.build(ResponseCode.SUCCESS, null, HttpStatus.OK);
        } catch (Exception e) {
            log.info("An error occurred while trying to delete existing section. Error : {}", e.getMessage());
            return ResponseUtil.build(ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
