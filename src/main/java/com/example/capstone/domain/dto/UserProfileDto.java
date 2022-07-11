package com.example.capstone.domain.dto;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.constant.AppConstant.*;
import com.example.capstone.domain.payload.response.UserResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserProfileDto {

    private Long id;

    private String phoneNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = AppConstant.DATE_JSON_FORMAT)
    private LocalDate dateOfBirth;

    private Gender gender;

    private String employeeId;

    private String role;

    private String status;

    private String address;

    @JsonIgnore
    private String urlBucket;

    private String urlImage;

    @JsonIgnore
    private String imageFileName;

    private UserResponse user;

}
