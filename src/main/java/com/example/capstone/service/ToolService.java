package com.example.capstone.service;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.constant.BucketName;
import com.example.capstone.domain.dao.Course;
import com.example.capstone.domain.dao.Tool;
import com.example.capstone.domain.dto.CategoryDto;
import com.example.capstone.domain.dto.CourseDto;
import com.example.capstone.domain.dto.MentorDto;
import com.example.capstone.domain.dto.ToolDto;
import com.example.capstone.repository.CourseRepository;
import com.example.capstone.repository.ToolRepository;
import com.example.capstone.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.*;
import static org.apache.http.entity.ContentType.IMAGE_JPEG;

@Service
@Slf4j
public class ToolService {
    @Autowired
    private  UploadFileService uploadFileService;

    @Autowired
    private ToolRepository toolRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CourseRepository courseRepository;

    public ResponseEntity<Object> getAllToolByCourseId(Long courseId) {
        log.info("Executing get all Tool by Course Id {}", courseId);
        try {
            List<Tool> tools = toolRepository.findAllByCourseId(courseId);
            List<ToolDto> request = new ArrayList<>();
            for (Tool tool: tools){
                ToolDto toolDto = mapper.map(tool, ToolDto.class);
                request.add(toolDto);
            }
            log.info("Successfully retrieved all Tool");
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, request, HttpStatus.OK);
        } catch (Exception e){
            log.error("An error occurred while trying to get all tool. Error : {}", e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> createNewTool(Long courseId,ToolDto request, MultipartFile file) {
        log.info("Executing add new tool");
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
            String urlBucket = String.format("%s/%s/%s", BucketName.CONTENT_IMAGE.getBucketName(), "tool-images", uuid);
            String fileName = String.format("%s", file.getOriginalFilename());
            String urlImage = String.format("%s/%s/%s/%s",pattern,"tool-images",uuid,fileName);
            uploadFileService.upload(urlBucket, fileName, Optional.of(metadata), file.getInputStream());

            //and then save category in database
            Optional<Course> course = courseRepository.findById(courseId);
            Tool tool = mapper.map(request, Tool.class);
            tool.setCourse(course.get());
            tool.setImageFileName(fileName);
            tool.setUrlBucket(urlBucket);
            tool.setUrlImage(urlImage);
            toolRepository.save(tool);


            log.info("Successfully added new tool");
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, mapper.map(tool, ToolDto.class), HttpStatus.OK);
        } catch (Exception e) {
            log.error("An error occurred while trying to add new tool. Error : {}", e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> updateById(Long id, ToolDto request, MultipartFile file) {
        log.info("Executing update tool with Id [{}]", id);
        try {
            Optional<Tool> optionalTool = toolRepository.findById(id);
            if (optionalTool.isEmpty()){
                log.info("Tool with Id [{}] not found",id);
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND,null,HttpStatus.BAD_REQUEST);
            }
            if (file == null) {
                log.info("File is null, update tool without file");
                optionalTool.ifPresent(tool -> {
                    tool.setId(id);
                    tool.setName(request.getName());
                    tool.setLink(request.getLink());
                    toolRepository.save(tool);
                });

                log.info("Successfully updated tool without image with Id : [{}]",id);
            } else {
              log.info("File is not null, updated tool with file");

                // delete path old
                Tool oldTool = toolRepository.findById(id).get();
                uploadFileService.delete(oldTool.getUrlBucket(),oldTool.getImageFileName());

                //get file metadata
                Map<String, String> metadata = new HashMap<>();
                metadata.put("Content-Type", file.getContentType());
                metadata.put("Content-Length", String.valueOf(file.getSize()));

                //save image in s3
                String pattern = "https://capstone-lms-storage.s3.amazonaws.com";
                String uuid = UUID.randomUUID().toString().replace("-","");
                String urlBucket = String.format("%s/%s/%s", BucketName.CONTENT_IMAGE.getBucketName(), "tool-images", uuid);
                String fileName = String.format("%s", file.getOriginalFilename());
                String urlImage = String.format("%s/%s/%s/%s",pattern,"tool-images",uuid,fileName);
                uploadFileService.upload(urlBucket, fileName, Optional.of(metadata), file.getInputStream());

                optionalTool.ifPresent(tool -> {
                    tool.setId(id);
                    tool.setName(request.getName());
                    tool.setLink(request.getLink());
                    tool.setUrlBucket(urlBucket);
                    tool.setUrlImage(urlImage);
                    tool.setImageFileName(fileName);
                    toolRepository.save(tool);
                });

                log.info("Successfully update tool with Image with Id : [{}]",id);
            }
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS,mapper.map(optionalTool.get(), ToolDto.class),HttpStatus.OK);
        } catch (Exception e) {
            log.info("An error occurred while trying to update existing tool. Error : {}", e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
