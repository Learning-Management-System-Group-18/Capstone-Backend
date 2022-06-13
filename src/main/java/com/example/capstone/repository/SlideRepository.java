package com.example.capstone.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.capstone.domain.dao.Slide;

@Repository
public interface SlideRepository extends JpaRepository<Slide, Long>{
    Page<Slide> findBySectionId(Long id, Pageable pageable);
    boolean existsByTitle(String title);
}
