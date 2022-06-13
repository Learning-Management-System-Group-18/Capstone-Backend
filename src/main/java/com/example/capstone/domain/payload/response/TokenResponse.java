package com.example.capstone.domain.payload.response;

import com.example.capstone.constant.AppConstant;
import com.example.capstone.domain.dao.Role;
import lombok.Data;

import java.util.Set;


@Data
public class TokenResponse {
    private String token;
    private Set<String> role;
}
