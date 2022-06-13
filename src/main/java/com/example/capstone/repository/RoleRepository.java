package com.example.capstone.repository;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.domain.dao.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(AppConstant.RoleType name);
}
