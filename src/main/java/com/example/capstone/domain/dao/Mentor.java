package com.example.capstone.domain.dao;

import com.example.capstone.domain.common.BaseDAO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mentor")
@SQLDelete(sql = "UPDATE mentor SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class Mentor extends BaseDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String urlBucket;

    private String urlImage;

    private String imageFileName;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
