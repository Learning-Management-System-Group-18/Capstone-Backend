package com.example.capstone.repository;

import com.example.capstone.domain.dao.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Long> {
    List<Tool> findAllByCourseId(Long courseId);
}
