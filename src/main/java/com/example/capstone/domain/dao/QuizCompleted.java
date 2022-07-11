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

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "quizCompleted")
@SQLDelete(sql = "UPDATE quizCompleted SET is_deleted = true WHERE user_id = ? and course_id = ?")
@Where(clause = "is_deleted = false")
@IdClass(QuizCompleted.QuizCompletedId.class)
public class QuizCompleted extends BaseDAO {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuizCompletedId implements Serializable {
        private static final long serialVersionUID = -8691658319583845607L;
        private Long user;
        private Long quiz;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "slide_id")
    private Quiz quiz;

}
