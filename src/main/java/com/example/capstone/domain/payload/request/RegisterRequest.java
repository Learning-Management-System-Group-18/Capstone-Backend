package com.example.capstone.domain.payload.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterRequest {
    @Schema(example = "Ilham Hidayat")
    @NotEmpty(message = "name shouldn't be empty")
    private String fullName;

    @Schema(example = "ilham@gmail.com")
    @Email(message = "invalid email address")
    private String email;

    @Schema(example = "aegon213")
    @Size(min=8, message = "password must be greater than or equal to 8")
    private String password;

}
