package com.example.capstone.repository;

import com.example.capstone.domain.dao.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.capstone.domain.dao.Slide;

import java.util.List;

@Repository
public interface SlideRepository extends JpaRepository<Slide, Long>{
    List<Slide> findAllBySectionId(Long sectionId);
    boolean existsByTitle(String title);

    @Query("SELECT COUNT (p) FROM Slide p WHERE p.section.course.id = ?1")
    Integer countAllSlide(Long courseId);
}
