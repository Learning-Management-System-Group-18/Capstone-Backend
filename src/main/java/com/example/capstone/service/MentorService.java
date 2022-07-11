package com.example.capstone.service;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.constant.BucketName;
import com.example.capstone.domain.dao.Course;
import com.example.capstone.domain.dao.Mentor;
import com.example.capstone.domain.dto.MentorDto;
import com.example.capstone.repository.CourseRepository;
import com.example.capstone.repository.MentorRepository;
import com.example.capstone.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.apache.http.entity.ContentType.*;
import static org.apache.http.entity.ContentType.IMAGE_JPEG;

@Service
@Slf4j
public class MentorService {
    @Autowired
    private  UploadFileService uploadFileService;

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CourseRepository courseRepository;

    public ResponseEntity<Object> getAllMentorByCourseId(Long courseId) {
        log.info("Executing get all mentor by Course Id {}", courseId);
        try {
            List<Mentor> mentors = mentorRepository.findAllByCourseId(courseId);

            List<MentorDto> request = new ArrayList<>();
            for (Mentor mentor: mentors){
                MentorDto mentorDto = mapper.map(mentor, MentorDto.class);
                request.add(mentorDto);
            }
            log.info("Successfully retrieved all mentor");
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, request, HttpStatus.OK);
        } catch (Exception e){
            log.error("An error occurred while trying to get all mentor. Error : {}", e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> createNewMentor(Long courseId,MentorDto request, MultipartFile file) {
        log.info("Executing add new mentor");
        try {
            if (file.isEmpty()) {
                log.info("Cannot upload empty file");
                return ResponseUtil.build(AppConstant.ResponseCode.IMAGE_EMPTY,null,HttpStatus.BAD_REQUEST);
            }

            //get file metadata
            Map<String, String> metadata = new HashMap<>();
            metadata.put("Content-Type", file.getContentType());
            metadata.put("Content-Length", String.valueOf(file.getSize()));

            //save image in s3
            String pattern = "https://capstone-lms-storage.s3.amazonaws.com";
            String uuid = UUID.randomUUID().toString().replace("-","");
            String urlBucket = String.format("%s/%s/%s", BucketName.CONTENT_IMAGE.getBucketName(), "mentor-images", uuid);
            String fileName = String.format("%s", file.getOriginalFilename());
            String urlImage = String.format("%s/%s/%s/%s",pattern,"mentor-images",uuid,fileName);
            uploadFileService.upload(urlBucket, fileName, Optional.of(metadata), file.getInputStream());

            //and then save category in database
            Optional<Course> course = courseRepository.findById(courseId);
            Mentor mentor = mapper.map(request, Mentor.class);
            mentor.setCourse(course.get());
            mentor.setImageFileName(fileName);
            mentor.setUrlBucket(urlBucket);
            mentor.setUrlImage(urlImage);
            mentorRepository.save(mentor);


            log.info("Successfully added new mentor");
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, mapper.map(mentor, MentorDto.class), HttpStatus.OK);
        } catch (Exception e) {
            log.error("An error occurred while trying to add new mentor. Error : {}", e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> updateById(Long id, MentorDto request, MultipartFile file) {
        log.info("Executing update mentor with Id [{}]", id);
        try {
            Optional<Mentor> optionalMentor = mentorRepository.findById(id);
            if (optionalMentor.isEmpty()){
                log.info("Mentor with Id [{}] not found",id);
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND,null,HttpStatus.BAD_REQUEST);
            }
            if (file == null) {
                log.info("File is null, update mentor without file");
                optionalMentor.ifPresent(mentor -> {
                    mentor.setId(id);
                    mentor.setName(request.getName());
                    mentorRepository.save(mentor);
                });

                log.info("Successfully updated mentor without image with Id : [{}]",id);
            } else {
                log.info("File is not null, updated mentor with file");

                // delete path old
                Mentor oldMentor = mentorRepository.findById(id).get();
                uploadFileService.delete(oldMentor.getUrlBucket(),oldMentor.getImageFileName());

                //get file metadata
                Map<String, String> metadata = new HashMap<>();
                metadata.put("Content-Type", file.getContentType());
                metadata.put("Content-Length", String.valueOf(file.getSize()));

                //save image in s3
                String pattern = "https://capstone-lms-storage.s3.amazonaws.com";
                String uuid = UUID.randomUUID().toString().replace("-","");
                String urlBucket = String.format("%s/%s/%s", BucketName.CONTENT_IMAGE.getBucketName(), "mentor-images", uuid);
                String fileName = String.format("%s", file.getOriginalFilename());
                String urlImage = String.format("%s/%s/%s/%s",pattern,"mentor-images",uuid,fileName);
                uploadFileService.upload(urlBucket, fileName, Optional.of(metadata), file.getInputStream());

                optionalMentor.ifPresent(mentor -> {
                    mentor.setId(id);
                    mentor.setName(request.getName());
                    mentor.setUrlBucket(urlBucket);
                    mentor.setUrlImage(urlImage);
                    mentor.setImageFileName(fileName);
                    mentorRepository.save(mentor);
                });

                log.info("Successfully update mentor with Image with Id : [{}]",id);
            }
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS,mapper.map(optionalMentor.get(), MentorDto.class),HttpStatus.OK);
        } catch (Exception e) {
            log.info("An error occurred while trying to update existing Mentor. Error : {}", e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
