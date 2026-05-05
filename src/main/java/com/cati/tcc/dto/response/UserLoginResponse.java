package com.cati.tcc.dto.response;

public record UserLoginResponse(String token, UserResponse userData) {
}
