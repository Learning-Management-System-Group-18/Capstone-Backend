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
@Table(name = "slideCompleted")
@SQLDelete(sql = "UPDATE slideCompleted SET is_deleted = true WHERE user_id = ? and course_id = ?")
@Where(clause = "is_deleted = false")
@IdClass(SlideCompleted.SlideCompletedId.class)
public class SlideCompleted extends BaseDAO {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SlideCompletedId implements Serializable {
        private static final long serialVersionUID = -3484684671764445624L;
        private Long user;
        private Long slide;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "slide_id")
    private Slide slide;

}
