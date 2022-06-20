package com.example.capstone.repository;

import com.example.capstone.domain.dao.Section;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.capstone.domain.dao.Video;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long>{
    Page<Video> findAllBySectionId(Long sectionId, Pageable pageable);
    boolean existsByTitle(String title);
}
