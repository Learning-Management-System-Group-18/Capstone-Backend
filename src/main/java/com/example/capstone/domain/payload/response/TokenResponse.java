package com.example.capstone.domain.payload.response;

import lombok.Data;

import java.util.Set;


@Data
public class TokenResponse {
    private String token;
    private Set<String> role;
}
