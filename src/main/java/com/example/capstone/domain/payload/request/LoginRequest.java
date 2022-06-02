package com.example.capstone.domain.payload.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequest {

    @Schema(example = "ilham@gmail.com")
    private String email;

    @Schema(example = "aegon213")
    private String password;
}
