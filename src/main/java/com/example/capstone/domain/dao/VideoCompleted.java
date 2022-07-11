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
@Table(name = "videoCompleted")
@SQLDelete(sql = "UPDATE videoCompleted SET is_deleted = true WHERE course_id = ? and user_id = ?")
@Where(clause = "is_deleted = false")
@IdClass(VideoCompleted.VideoCompletedId.class)
public class VideoCompleted extends BaseDAO {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VideoCompletedId implements Serializable {
        private static final long serialVersionUID = -7607771355184002587L;
        private Long user;
        private Long video;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "video_id")
    private Video video;

}
