package com.example.capstone.repository;

import com.example.capstone.domain.dao.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MentorRepository extends JpaRepository<Mentor, Long> {
    List<Mentor> findAllByCourseId(Long courseId);

    List<Mentor> findTop10ByOrderById();

}
