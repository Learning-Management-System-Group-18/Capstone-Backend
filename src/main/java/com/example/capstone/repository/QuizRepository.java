package com.example.capstone.repository;

import com.example.capstone.domain.dao.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.capstone.domain.dao.Quiz;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long>{
    Page<Quiz> findAllBySectionId(Long sectionId, Pageable pageable);
    boolean existsByTitle(String title);

    @Query("SELECT COUNT (p) FROM Quiz p WHERE p.section.course.id = ?1")
    Integer countAllQuiz(Long courseId);
}
