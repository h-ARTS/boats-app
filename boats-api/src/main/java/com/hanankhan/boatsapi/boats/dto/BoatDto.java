package com.hanankhan.boatsapi.boats.dto;

import java.time.Instant;

public record BoatDto(
        Long id,
        String name,
        String type,
        Float length,
        String description,
        Instant createdAt,
        Instant updatedAt) {
}
