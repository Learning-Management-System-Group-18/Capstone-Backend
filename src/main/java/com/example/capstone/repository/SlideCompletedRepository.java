package com.example.capstone.repository;

import com.example.capstone.domain.dao.SlideCompleted;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SlideCompletedRepository extends JpaRepository<SlideCompleted,Long> {
    Boolean existsByUserIdAndSlideId(Long userId, Long slideId);

    @Query("SELECT COUNT(p) from SlideCompleted p where p.user.id = ?1 and p.slide.section.course.id = ?2")
    Integer countSlide(Long userId, Long slideId);
}
