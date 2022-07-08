package com.example.capstone.domain.dto;

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
import org.hibernate.annotations.ColumnDefault;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CategoryDto {
    private Long id;

    private String title;

    private String description;

    @JsonIgnore
    private String urlBucket;

    private String urlImage;

    @JsonIgnore
    private String imageFileName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer countCourse ;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer countUser ;

}
