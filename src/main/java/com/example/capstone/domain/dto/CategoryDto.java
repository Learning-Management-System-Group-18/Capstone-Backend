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
public class CategoryDto {
    @Schema(type = "long", example = "1", description = "ID Category", hidden = true)
    private Long id;

    @Schema(type = "string", example = "Java", description = "nama category")
    private String title;
}
