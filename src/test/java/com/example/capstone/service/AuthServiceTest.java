package com.example.capstone.service;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.domain.common.ApiResponse;
import com.example.capstone.domain.common.ApiResponseStatus;
import com.example.capstone.domain.dao.Role;
import com.example.capstone.domain.dao.User;
import com.example.capstone.domain.payload.request.LoginRequest;
import com.example.capstone.domain.payload.request.RegisterRequest;
import com.example.capstone.domain.payload.response.RegisterResponse;
import com.example.capstone.domain.payload.response.TokenResponse;
import com.example.capstone.repository.RoleRepository;
import com.example.capstone.repository.UserProfileRepository;
import com.example.capstone.repository.UserRepository;
import com.example.capstone.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AuthService.class)
class AuthServiceTest {
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserProfileRepository userProfileRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private AuthService authService;



    @Test
    void registerSuccess_Test() {
        RegisterRequest request = new RegisterRequest(
                "Ilham Hidayat",
                "ilham@gmail.com",
                "divel213"
        );

        RegisterResponse response = new RegisterResponse(
                "Ilham Hidayat",
                "ilham@gmail.com"
        );

        User user = User.builder()
                .id(1L)
                .email("ilham@gmail.com")
                .build();
        Role role = new Role(1L, AppConstant.RoleType.ROLE_USER);


        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed_password");
        when(modelMapper.map(any(),eq(User.class))).thenReturn(user);
        when(roleRepository.findByName(any())).thenReturn(Optional.of(role));
        when(userRepository.save(any())).thenReturn(user);
        when(modelMapper.map(any(), eq(RegisterResponse.class))).thenReturn(response);

        ResponseEntity responseEntity = authService.register(request);
        ApiResponse apiResponse = ((ApiResponse) responseEntity.getBody());



        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("ilham@gmail.com", ((RegisterResponse) apiResponse.getData()).getEmail());
        assertEquals("Ilham Hidayat", ((RegisterResponse) apiResponse.getData()).getFullName());
    }

    @Test
    void registerExist_Test(){
        RegisterRequest request = new RegisterRequest(
                "Ilham Hidayat",
                "ilham@gmail.com",
                "divel213"
        );

        //stubbing
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        ResponseEntity responseEntity = authService.register(request);
        ApiResponse apiResponse = ((ApiResponse) responseEntity.getBody());

        //verification
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Email is already registered", apiResponse.getData().toString());
    }

    @Test
    void generateTokenSuccess_Test() {
        LoginRequest request = new LoginRequest();
        request.setEmail("ilham@gmail.com");
        request.setPassword("pass");
        Authentication authentication = new UsernamePasswordAuthenticationToken("uname", "password");


        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtTokenProvider.generateToken(any())).thenReturn("token");
        User user = mock(User.class);
        Authentication authentication1 = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication1);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);
        ResponseEntity responseEntity = authService.generateToken(request);
        ApiResponse apiResponse = ((ApiResponse) responseEntity.getBody());

        //verification
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("token", ((TokenResponse) apiResponse.getData()).getToken());
        assertTrue(((TokenResponse) apiResponse.getData()).getRole().isEmpty() );
    }

    @Test
    void generateTokenBadCredentials_Test() {
        LoginRequest request = new LoginRequest();
        request.setEmail("ilham@gmail.com");
        request.setPassword("salah");

        //stubbing
        when(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException.class);

        ResponseEntity responseEntity = authService.generateToken(request);
        ApiResponse apiResponse = ((ApiResponse) responseEntity.getBody());
        String code = ((ApiResponseStatus) apiResponse.getStatus()).getCode();

        //verification
        assertEquals("BAD_CREDENTIALS", code);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
    @Test
    void generateTokenError_Test() {
        LoginRequest request = new LoginRequest();
        request.setEmail("ilham@gmail.com");
        request.setPassword("salah");

        //stubbing
        when(authenticationManager.authenticate(any())).thenThrow(NullPointerException.class);

        ResponseEntity responseEntity = authService.generateToken(request);
        ApiResponse apiResponse = ((ApiResponse) responseEntity.getBody());
        String code = ((ApiResponseStatus) apiResponse.getStatus()).getCode();

        //verfication
        assertEquals("UNKNOWN_ERROR", code);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

}