package com.example.capstone.domain.dao;

import com.example.capstone.domain.common.BaseDAO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category")
@SQLDelete(sql = "UPDATE category SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class Category extends BaseDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String urlBucket;

    private String urlImage;

    private String imageFileName;

    private Integer countUser;

    private Integer countCourse ;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category", orphanRemoval = true)
    private List<Course> courses;
}
