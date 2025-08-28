package com.hanankhan.boatsapi.boats.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BoatUpsertDto(@NotBlank(message = "Name should not be blank.") String name,
                            @NotBlank(message = "Type should not be blank.") String type,
                            @DecimalMin(value = "2.5", message = "Boat length must be at least 2.5 meters.")
                            @NotNull(message = "Boat length must not be empty.") Float length,
                            @NotBlank(message = "Description should not be blank.") String description) {
}
