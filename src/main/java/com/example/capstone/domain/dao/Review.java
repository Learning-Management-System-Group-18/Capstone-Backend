package com.example.capstone.domain.dao;

import com.example.capstone.domain.common.BaseDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reviews")
@SQLDelete(sql = "UPDATE reviews SET is_deleted = true WHERE user_id = ? and course_id = ?")
@Where(clause = "is_deleted = false")
@IdClass(Review.ReviewId.class)
public class Review extends BaseDAO {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewId implements Serializable {
        private static final long serialVersionUID = 3749500003379413882L;
        private Long user;
        private Long course;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(columnDefinition = "text")
    private String review;

    @Column(nullable = false)
    private int rating;

    private LocalDateTime reviewDate;


}
