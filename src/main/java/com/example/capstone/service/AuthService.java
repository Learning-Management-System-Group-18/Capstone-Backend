package com.example.capstone.service;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.domain.dao.Role;
import com.example.capstone.domain.dao.User;
import com.example.capstone.domain.payload.request.LoginRequest;
import com.example.capstone.domain.payload.request.RegisterRequest;
import com.example.capstone.domain.payload.response.RegisterResponse;
import com.example.capstone.domain.payload.response.TokenResponse;
import com.example.capstone.repository.RoleRepository;
import com.example.capstone.repository.UserRepository;
import com.example.capstone.security.JwtTokenProvider;
import com.example.capstone.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
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

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    public ResponseEntity<Object> register(RegisterRequest req) {
        log.info("Executing register new user");

        if (userRepository.existsByEmail(req.getEmail())) {
            log.info("User with email : [{}] already exist, aborting register", req.getEmail());
            return ResponseUtil.build(
                    AppConstant.ResponseCode.BAD_CREDENTIALS,
                    "Email is already registered",
                    HttpStatus.BAD_REQUEST
            );
        }


        log.info("User doesnt exist yet, creating new user");
        User user = modelMapper.map(req, User.class);
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        Set<Role> roles = new HashSet<>();

//         Temporary code to insert values to Role table, comment on productions
//        Optional<Role> roleOptional = roleRepository.findByName(AppConstant.RoleType.ROLE_USER);
//        if(roleOptional.isEmpty()){
//            Role userRole = new Role();
//            userRole.setName(AppConstant.RoleType.ROLE_USER);
//
//            Role adminRole = new Role();
//            adminRole.setName(AppConstant.RoleType.ROLE_ADMIN);
//
//            roleRepository.save(userRole);
//            roleRepository.save(adminRole);
//        }
//         end of temporary code

        roleRepository.findByName(AppConstant.RoleType.ROLE_USER).ifPresent(roles::add);

        user.setRoles(roles);
        userRepository.save(user);
        return ResponseUtil.build(
                AppConstant.ResponseCode.SUCCESS,
                modelMapper.map(user, RegisterResponse.class),
                HttpStatus.OK
        );
    }

    public ResponseEntity<Object> generateToken(LoginRequest req) {
        log.info("Generating JWT based on provided email and password");
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            req.getEmail(),
                            req.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Set<String> roles = SecurityContextHolder.getContext().getAuthentication()
                    .getAuthorities().stream().map(r -> r.getAuthority()).collect(Collectors.toSet());
            User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String jwt = jwtTokenProvider.generateToken(authentication);
            TokenResponse tokenResponse = new TokenResponse();
            tokenResponse.setFullName(auth.getFullName());
            tokenResponse.setToken(jwt);
            tokenResponse.setRole(roles);


            return ResponseUtil.build(AppConstant.ResponseCode.SUCCESS, tokenResponse, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            log.error("Bad Credential", e);
            return ResponseUtil.build(
                    AppConstant.ResponseCode.BAD_CREDENTIALS,
                    "Email or password is incorrect",
                    HttpStatus.BAD_REQUEST
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseUtil.build(
                    AppConstant.ResponseCode.UNKNOWN_ERROR,
                    "null",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
