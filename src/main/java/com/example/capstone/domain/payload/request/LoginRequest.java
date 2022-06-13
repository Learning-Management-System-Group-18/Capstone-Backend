package com.example.capstone.domain.payload.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequest {

    @Schema(example = "ilham@gmail.com")
    private String email;

    @Schema(example = "aegon213")
    private String password;
}
