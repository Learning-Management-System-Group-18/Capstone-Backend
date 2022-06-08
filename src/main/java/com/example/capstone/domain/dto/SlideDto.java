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
public class SlideDto {
    @Schema(type = "long", example = "1", description = "Id Slide", hidden = true)
    private Long id;

    @Schema(type = "string", example = "Slide Intrdouce java", description = "Title Slide")
    private String title;

    @Schema(type = "string", example = "Description slide introduce java", description = "Description Slide")
    private String description;

    @Schema(type = "string", example = "docs.google.com/presentation", description = "Link Slide")
    private String link;

    @Schema(hidden = true)
    private SectionDto section;
}
