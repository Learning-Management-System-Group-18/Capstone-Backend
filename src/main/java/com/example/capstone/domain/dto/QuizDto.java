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
public class QuizDto {
    @Schema(type = "long", example = "1", description = "Id Quiz", hidden = true)
    private Long id;

    @Schema(type = "string", example = "Quiz Intrdouce java", description = "Title Quiz")
    private String title;

    @Schema(type = "string", example = "Description quiz introduce java", description = "Description Quiz")
    private String description;

    @Schema(type = "string", example = "docs.google.com/forms/", description = "Link Quiz")
    private String link;

    @Schema(hidden = true)
    private SectionDto section;
}
