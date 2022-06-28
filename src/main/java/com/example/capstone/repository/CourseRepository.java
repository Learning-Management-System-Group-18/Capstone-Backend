package com.example.capstone.repository;

import com.example.capstone.domain.dao.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> , JpaSpecificationExecutor<Course> {
    boolean existsByTitle(String title);
    Page<Course> findAllByCategoryId(Long categoryId, Pageable pageable);
    Integer countCourseByCategoryId(Long id);
}
