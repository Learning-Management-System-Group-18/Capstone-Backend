package com.example.capstone.repository;

import com.example.capstone.domain.dao.QuizCompleted;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuizCompletedRepository extends JpaRepository<QuizCompleted, Long> {
    Boolean existsByUserIdAndQuizId(Long userId, Long quizId);

    @Query("SELECT COUNT(p) from QuizCompleted p where p.user.id = ?1 and p.quiz.section.course.id = ?2")
    Integer countQuiz(Long userId, Long quizId);
}
