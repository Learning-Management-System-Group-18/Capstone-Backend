package com.example.capstone.repository;

import com.example.capstone.domain.dao.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByCourseIdAndUserId(Long courseId, Long userId);
    Page<Review> findAllByCourseId(Long courseId, Pageable pageable);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.course.id = ?1")
    Double averageOfCourseReviewRating(Long courseId);
}
