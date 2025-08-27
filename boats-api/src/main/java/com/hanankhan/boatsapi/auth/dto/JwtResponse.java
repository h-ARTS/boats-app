package com.hanankhan.boatsapi.auth.dto;

public record JwtResponse(String accessToken, long expiresIn) {
}
