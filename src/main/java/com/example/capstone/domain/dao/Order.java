package com.example.capstone.domain.dao;

import com.example.capstone.domain.common.BaseDAO;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.*;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.stereotype.Component;


import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
@SQLDelete(sql = "UPDATE orders SET is_deleted = true WHERE user_id = ? and course_id = ?")
@Where(clause = "is_deleted = false")
@IdClass(Order.OrderId.class)
public class Order extends BaseDAO {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderId implements Serializable {
        private static final long serialVersionUID = -7142977676038489976L;
        private Long user;
        private Long course;
    }

    private String id;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private LocalDateTime orderDate;

}
