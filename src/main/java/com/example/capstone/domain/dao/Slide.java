package com.example.capstone.domain.dao;

import javax.persistence.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.example.capstone.domain.common.BaseDAO;

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
@Table(name = "slide")
@SQLDelete(sql = "UPDATE slide SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class Slide extends BaseDAO{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    private String link;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;
}
