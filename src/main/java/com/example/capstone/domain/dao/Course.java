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

import java.util.List;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "course")
@SQLDelete(sql = "UPDATE course SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class Course extends BaseDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;


    private double rating;

    private String description;

    private double rating;

    private String urlBucket;

    private String urlImage;

    private String description;

   



    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course",orphanRemoval = true)
    private List<Section> sections;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course",orphanRemoval = true)
    private List<Mentor> mentors;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course",orphanRemoval = true)
    private List<Tool> tools;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course", orphanRemoval = true)
    private List<Review> reviews;

}
