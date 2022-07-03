package com.example.capstone.repository;

import com.example.capstone.domain.dao.Order;
import com.example.capstone.domain.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    Integer countOrderByCourse_CategoryId(Long id);
    Order findAllByCourseId(Long id);

    List<Order> findOrderByUser(User user);
    boolean existsByCourseIdAndUserId(Long courseId, Long userId);
}
