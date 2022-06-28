package com.example.capstone.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SectionDto {
    @Schema(type = "long", example = "1", description = "ID Section", hidden = true)
    private Long id;

    @Schema(type = "string", example = "Introduce Java", description = "Nama Section")
    private String title;

    @Schema(hidden = true)
    private CourseDto course;
    
}
