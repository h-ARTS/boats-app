package com.hanankhan.boatsapi.boats.dto;

import jakarta.validation.constraints.NotBlank;

public record BoatUpsertDto(@NotBlank(message = "Name should not be blank") String name,
                            @NotBlank(message = "Description should not be blank") String description) {
}
