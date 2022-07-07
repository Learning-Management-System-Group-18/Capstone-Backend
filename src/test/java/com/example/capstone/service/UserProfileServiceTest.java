package com.example.capstone.service;

import com.example.capstone.repository.UserProfileRepository;
import com.example.capstone.repository.UserRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

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

}