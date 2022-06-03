package com.example.capstone.domain.payload.response;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.domain.dao.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterResponse {
    private String fullName;
    private String email;

}
