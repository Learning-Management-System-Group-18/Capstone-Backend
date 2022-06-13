package com.example.capstone.repository;

import com.example.capstone.domain.dao.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findByCategoryId(Long id, Pageable pageable);
    boolean existsByTitle(String title);
}
