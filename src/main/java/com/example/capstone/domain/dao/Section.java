package com.example.capstone.domain.dao;

import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.example.capstone.domain.common.BaseDAO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "section")
@SQLDelete(sql = "UPDATE section SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class Section extends BaseDAO{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "section")
    private List<Video> videos;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "section")
    private List<Slide> slides;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "section")
    private List<Quiz> quizs;
}
