package com.example.capstone.repository;

import com.example.capstone.domain.dao.VideoCompleted;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoCompletedRepository extends JpaRepository<VideoCompleted,Long> {
    Boolean existsByUserIdAndVideoId(Long userId, Long videoId);

    @Query("SELECT COUNT(p) from VideoCompleted p where p.user.id = ?1 and p.video.section.course.id = ?2")
    Integer countVideo(Long userId, Long courseId);

}
