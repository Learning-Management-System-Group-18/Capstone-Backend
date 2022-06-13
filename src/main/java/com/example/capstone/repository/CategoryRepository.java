package com.example.capstone.repository;

import com.example.capstone.domain.dao.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByTitle(String title);
    Category findByTitle(String title);
}
