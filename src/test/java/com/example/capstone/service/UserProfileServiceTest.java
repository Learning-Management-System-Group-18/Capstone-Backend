package com.example.capstone.service;

import com.example.capstone.domain.common.ApiResponse;
import com.example.capstone.domain.dao.User;
import com.example.capstone.domain.dao.UserProfile;
import com.example.capstone.domain.dto.UserProfileDto;
import com.example.capstone.repository.UserProfileRepository;
import com.example.capstone.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UserProfileService.class)
class UserProfileServiceTest {

    @MockBean
    private UploadFileService uploadFileService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserProfileRepository userProfileRepository;

    @MockBean
    private ModelMapper mapper;

    @Autowired
    private UserProfileService userProfileService;

    @Test
    void getProfileByEmail_Success() {
        User user = User.builder()
                .id(1L)
                .email("ilham@gmail.com")
                .build();

        UserProfile userProfile = UserProfile.builder()
                .id(1L)
                .build();

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(userProfileRepository.findUserProfileByEmail(anyString())).thenReturn(Optional.of(userProfile));
        when(mapper.map(any(), eq(UserProfile.class))).thenReturn(userProfile);

        ResponseEntity<Object> responseEntity = userProfileService.getProfileByEmail("email");
        ApiResponse apiResponse = ((ApiResponse) responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }

    @Test
    void getProfileByEmail_EmailNotFound() {
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = userProfileService.getProfileByEmail("email");
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void getProfileByEmail_Error() {
        when(userRepository.findUserByEmail(anyString())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = userProfileService.getProfileByEmail("email");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    void updateProfile_Success() {
        User user = User.builder()
                .id(1L)
                .email("ilham@gmail.com")
                .build();

        UserProfile userProfile = UserProfile.builder()
                .id(1L)
                .build();

        UserProfileDto userProfileDto = UserProfileDto.builder()
                .id(1L)
                .build();

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(userProfileRepository.findUserProfileByEmail(anyString())).thenReturn(Optional.of(userProfile));
        when(userProfileRepository.save(any())).thenReturn(userProfile);
        when(mapper.map(any(),eq(UserProfileDto.class))).thenReturn(userProfileDto);

        ResponseEntity<Object> responseEntity = userProfileService.updateProfile(userProfileDto,"email");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void updateProfile_EmailNotFound() {
        UserProfileDto userProfileDto = UserProfileDto.builder()
                .id(1L)
                .build();
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());
        ResponseEntity<Object> responseEntity = userProfileService.updateProfile(userProfileDto,"email");
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void updateProfile_UserProfileNotFound() {
        User user = User.builder()
                .id(1L)
                .email("ilham@gmail.com")
                .build();

        UserProfileDto userProfileDto = UserProfileDto.builder()
                .id(1L)
                .build();

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(userProfileRepository.findUserProfileByEmail(anyString())).thenReturn(Optional.empty());

        ResponseEntity<Object> responseEntity = userProfileService.updateProfile(userProfileDto,"email");
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

    @Test
    void updateProfile_Error() {
        UserProfileDto userProfileDto = UserProfileDto.builder()
                .id(1L)
                .build();
        when(userRepository.findUserByEmail(anyString())).thenThrow(NullPointerException.class);
        ResponseEntity<Object> responseEntity = userProfileService.updateProfile(userProfileDto,"email");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

    }
}