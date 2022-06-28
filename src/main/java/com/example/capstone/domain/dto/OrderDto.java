package com.example.capstone.domain.dto;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.domain.payload.response.RegisterResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDto {

    private String id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstant.DATE_JSON_FORMAT)
    private LocalDateTime orderDate;

    private RegisterResponse user;

    private CourseDto course;

}
