package com.example.capstone.repository;

import com.example.capstone.domain.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<User, Long > {
    Optional<User> findUserByEmail(String email);

    boolean existsByEmail(String email);


}
