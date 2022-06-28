package com.example.capstone.domain.dto;

import com.example.capstone.domain.payload.response.QuizResponse;
import com.example.capstone.domain.payload.response.SlideResponse;
import com.example.capstone.domain.payload.response.VideoResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ContentDto {
    private List<VideoResponse> video;

    private List<SlideResponse> slide;

    private List<QuizResponse> quiz;
}

