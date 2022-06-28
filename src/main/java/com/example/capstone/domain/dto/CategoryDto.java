package com.example.capstone.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class CategoryDto {
    @Schema(type = "long", example = "1", description = "ID Category", hidden = true)
    private Long id;

    @Schema(type = "string", example = "Android Developer", description = "nama category")
    private String title;

    @Schema(type = "string", example = "Android Developer adalah course yang mempelajari", description = "description category")
    private String description;

    @Schema(hidden = true)
    @JsonIgnore
    private String urlBucket;

    @Schema(hidden = true)
    private String urlImage;

    @Schema(hidden = true)
    private String imageFileName;


}
