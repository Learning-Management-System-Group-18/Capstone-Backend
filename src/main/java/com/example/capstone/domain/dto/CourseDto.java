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
public class CourseDto {
    @Schema(type = "long", example = "1", description = "ID Course", hidden = true)
    private Long id;

    @Schema(type = "string", example = "Trust With Java", description = "Nama Course")
    private String title;

    @Schema(type = "double", example = "4.5", description = "Rating Course")
    private double rating;

    @Schema(type = "string", example = "Learning java is good", description = "Deskripsi Course")
    private String description;

    @Schema(type = "String", example = "https://gambarjava", description = "Picture Course")
    private String thumbnail;

    @Schema(hidden = true)
    private CategoryDto category;

}
