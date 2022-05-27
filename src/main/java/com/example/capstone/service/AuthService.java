package com.example.capstone.service;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.domain.dao.User;
import com.example.capstone.domain.dto.UserDTO;
import com.example.capstone.domain.payload.EmailPassword;
import com.example.capstone.domain.payload.TokenResponse;
import com.example.capstone.repository.UserRepository;
import com.example.capstone.security.JwtTokenProvider;
import com.example.capstone.util.ResponseUtil;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    public ResponseEntity<Object> register(EmailPassword req, String username) {
        log.info("Executing register new user");
        Optional<User> userOptional = userRepository.getDistinctTopByUsername(req.getEmail());

        if (userOptional.isPresent()) {
            log.info("User with email : [{}] already exist, aborting register", req.getEmail());
            return ResponseUtil.build(
                    AppConstant.ResponseCode.BAD_CREDENTIALS,
                    "Email already exist",
                    HttpStatus.BAD_REQUEST
            );
        }

        log.info("User doesnt exist yet, creating new user");
        User user = new User();
        user.setUsername(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setCreatedBy(username);
        userRepository.save(user);
        log.info("User doesnt exist yet, creating new user");
        return ResponseUtil.build(
                AppConstant.ResponseCode.SUCCESS,
                modelMapper.map(user, UserDTO.class),
                HttpStatus.OK
        );
    }

    public ResponseEntity<Object> generateToken(EmailPassword req) {
        log.info("Generating JWT based on provided email and password");
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            req.getEmail(),
                            req.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.generateToken(authentication);
            TokenResponse tokenResponse = new TokenResponse();
            tokenResponse.setToken(jwt);

            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, tokenResponse, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            log.error("Bad Credential", e);
            return ResponseUtil.build(
                    AppConstant.ResponseCode.BAD_CREDENTIALS,
                    "Email or Password is incorrect",
                    HttpStatus.BAD_REQUEST
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseUtil.build(
                    AppConstant.ResponseCode.UNKNOWN_ERROR,
                    null,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
