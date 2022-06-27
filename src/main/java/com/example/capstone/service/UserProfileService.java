package com.example.capstone.service;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.domain.dao.User;
import com.example.capstone.domain.dao.UserProfile;
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

import java.util.Optional;

@Service
@Slf4j
public class UserProfileService {
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
            profile.setEmployeeId(request.getEmployeeId());
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


}
