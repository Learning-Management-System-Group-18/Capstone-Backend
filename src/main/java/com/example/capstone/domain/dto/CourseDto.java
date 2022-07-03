package com.example.capstone.domain.dto;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.domain.payload.response.CategoryResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CourseDto {
    private Long id;

    private String title;

    private double rating;

    private String description;

    private AppConstant.Level level;

    private Integer notFinish;

    @JsonIgnore
    private String urlBucket;

    private String urlImage;

    @JsonIgnore
    private String imageFileName;

    private CategoryResponse category;

}
