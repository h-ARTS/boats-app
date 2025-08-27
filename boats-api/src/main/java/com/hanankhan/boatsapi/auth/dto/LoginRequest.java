package com.hanankhan.boatsapi.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(@NotBlank(message = "Username can not be empty.")
                           @Size(min = 2, message = "Username must not be less than 2 characters") String username,
                           @NotBlank(message = "Password can not be empty.")
                           @Size(min = 8, max = 16, message = "Password must be between 8 and 16 characters") String password) {
}
