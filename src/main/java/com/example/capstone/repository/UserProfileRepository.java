package com.example.capstone.repository;

import com.example.capstone.domain.dao.User;
import com.example.capstone.domain.dao.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    @Query("select p from UserProfile p where p.user.email= ?1")
    Optional<UserProfile> findUserProfileByEmail(String email);


}
