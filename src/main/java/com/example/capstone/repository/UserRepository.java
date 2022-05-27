package com.example.capstone.repository;

import com.example.capstone.domain.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<User, Long > {
    Optional<User> getDistinctTopByUsername(String username);
}
