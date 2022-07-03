package com.example.capstone.repository;

import com.example.capstone.domain.dao.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.capstone.domain.dao.Section;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long>{
    Page<Section> findAllByCourseId(Long courseId, Pageable pageable);
    boolean existsByTitle(String title);


}
