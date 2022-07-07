package com.example.capstone.service;

import com.example.capstone.domain.dao.User;
import com.example.capstone.repository.UserRepository;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UserService.class)
class UserServiceTest {
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    void loadUserByUsername_Success_Test() {
        User user = User.builder().id(1L).email("ilham@gmail.com").build();

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("ilham@gmail.com");

        assertEquals("ilham@gmail.com", userDetails.getUsername());
    }

    @Test
    void loadUserByUsername_NotFound_Test() {
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> {
            UserDetails userDetails = userService.loadUserByUsername("ilham@gmail.com");
        });
    }
}
