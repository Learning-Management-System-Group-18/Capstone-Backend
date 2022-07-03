package com.example.capstone.service;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.constant.BucketName;
import com.example.capstone.domain.dao.Course;
import com.example.capstone.domain.dao.User;
import com.example.capstone.domain.dao.UserProfile;
import com.example.capstone.domain.dto.CourseDto;
import com.example.capstone.domain.dto.UserProfileDto;
import com.example.capstone.repository.UserProfileRepository;
import com.example.capstone.repository.UserRepository;
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
public class UserProfileService {

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private ModelMapper mapper;


    //get profile
    public ResponseEntity<Object> getProfileByEmail(String email) {
        log.info("Executing get user profile");
        try {
            Optional<User> optionalUser = userRepository.findUserByEmail(email);
            if (optionalUser.isEmpty()) {
                log.info("User with Email [{}] not found ", email);
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND,
                        null,
                        HttpStatus.BAD_REQUEST);
            }
            Optional<UserProfile> userProfile = userProfileRepository.findUserProfileByEmail(email);
            UserProfileDto request = mapper.map(userProfile, UserProfileDto.class);

            log.info("Successfully retrieved profile with email : {}", email);
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS,request,HttpStatus.OK);

        } catch (Exception e){
            log.error("An error occurred while trying to get profile by Email [{}]. Error : {}",
                    email,
                    e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,
                    null,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //update profile
    public ResponseEntity<Object> updateProfile(UserProfileDto request,String email){
        log.info("Executing get user profile");
        try {
            Optional<User> optionalUser = userRepository.findUserByEmail(email);
            if (optionalUser.isEmpty()) {
                log.info("User with Email [{}] not found ", email);
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND,
                        null,
                        HttpStatus.BAD_REQUEST);
            }

            Optional<UserProfile> optionalUserProfile = userProfileRepository.findUserProfileByEmail(email);
            if (optionalUserProfile.isEmpty()) {
                log.info("User profile with Email [{}] not found ", email);
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND,
                        null,
                        HttpStatus.BAD_REQUEST);
            }
            UserProfile profile = optionalUserProfile.get();
            profile.setUser(optionalUser.get());
            profile.setAddress(request.getAddress());
            profile.setPhoneNumber(request.getPhoneNumber());
            profile.setGender(request.getGender());
            profile.setDateOfBirth(request.getDateOfBirth());
            profile.setRole(request.getRole());
            profile.setStatus(request.getStatus());
            profile = userProfileRepository.save(profile);

            log.info("Successfully updated profile");
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS,
                    mapper.map(profile, UserProfileDto.class),
                    HttpStatus.OK);
    } catch (Exception e){
            log.error("An error occurred while trying to update profile by Email [{}]. Error : {}",
                    email,
                    e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,
                    null,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        }


    //add image
    public ResponseEntity<Object> updateImageUser(String email, MultipartFile file) {
        log.info("Executing update image existing user");
        try {
            Optional<UserProfile> optionalUser = userProfileRepository.findUserProfileByEmail(email);
            if (optionalUser.isEmpty()){
                log.info("User with Email [{}] not found",email);
                return ResponseUtil.build(AppConstant.ResponseCode.DATA_NOT_FOUND,null,HttpStatus.BAD_REQUEST);
            }

            //check if the file is an image
            if (!Arrays.asList(IMAGE_PNG.getMimeType(),
                    IMAGE_BMP.getMimeType(),
                    IMAGE_GIF.getMimeType(),
                    IMAGE_JPEG.getMimeType()).contains(file.getContentType())) {
                throw new IllegalStateException("FIle uploaded is not an image");
            }

            //get file metadata
            Map<String, String> metadata = new HashMap<>();
            metadata.put("Content-Type", file.getContentType());
            metadata.put("Content-Length", String.valueOf(file.getSize()));

            //save image in s3
            String pattern = "https://capstone-lms-storage.s3.amazonaws.com";
            String uuid = UUID.randomUUID().toString().replace("-","");
            String urlBucket = String.format("%s/%s/%s", BucketName.CONTENT_IMAGE.getBucketName(), "user-images", uuid);
            String fileName = String.format("%s", file.getOriginalFilename());
            String urlImage = String.format("%s/%s/%s/%s",pattern,"user-images",uuid,fileName);
            uploadFileService.upload(urlBucket, fileName, Optional.of(metadata), file.getInputStream());

            //and then save course in database
            optionalUser.ifPresent(profile -> {
                profile.setUrlBucket(urlBucket);
                profile.setUrlImage(urlImage);
                profile.setImageFileName(fileName);
                userProfileRepository.save(profile);
            });

            log.info("Successfully updated User Image with email : [{}]",email);
            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS,mapper.map(optionalUser.get(), UserProfileDto.class),HttpStatus.OK);
        } catch (Exception e) {
            log.info("An error occurred while trying to update existing User Image. Error : {}", e.getMessage());
            return ResponseUtil.build(AppConstant.ResponseCode.UNKNOWN_ERROR,null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
