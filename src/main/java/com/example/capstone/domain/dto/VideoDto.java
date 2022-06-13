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
public class VideoDto {
    @Schema(type = "long", example = "1", description = "Id Video", hidden = true)
    private Long id;

    @Schema(type = "string", example = "Video Intrdouce java", description = "Title Video")
    private String title;

    @Schema(type = "string", example = "Description video introduce java", description = "Description Video")
    private String description;

    @Schema(type = "string", example = "www.youtube.com", description = "Link Video")
    private String link;

    @Schema(hidden = true)
    private SectionDto section;

}
