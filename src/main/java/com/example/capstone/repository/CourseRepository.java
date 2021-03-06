package com.example.capstone.repository;

import com.example.capstone.domain.dao.Course;
import com.example.capstone.domain.dao.Mentor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> , JpaSpecificationExecutor<Course> {
    boolean existsByTitle(String title);
    Page<Course> findAllByCategoryId(Long categoryId, Pageable pageable);
    List<Course> findAllByCategoryId(Long categoryId);
    Integer countCourseByCategoryId(Long id);

    @Query("SELECT p FROM Course p ORDER BY p.countUser DESC")
    List<Course> popularCourse();


    List<Course> findTop10ByOrderByCountUserDesc();


}
