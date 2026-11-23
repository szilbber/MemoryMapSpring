package org.example.memorymapspring.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.example.memorymapspring.entity.BurialLocation;

public record BurialLocationDto(
        Long id,

        @DecimalMin(value = "-90", message = "Широта должна быть от -90 до 90")
        @DecimalMax(value = "90", message = "Широта должна быть от -90 до 90")
        Double latitude,

        @DecimalMin(value = "-180", message = "Долгота должна быть от -180 до 180")
        @DecimalMax(value = "180", message = "Долгота должна быть от -180 до 180")
        Double longitude,

        Boolean exact,

        String description,

        @NotNull(message = "Выберите человека")
        Long personId,

        String personName
) {

    public BurialLocationDto {
        exact = Boolean.TRUE.equals(exact);
    }

    public static BurialLocationDto empty() {
        return new BurialLocationDto(null, null, null, false, null, null, null);
    }

    public static BurialLocationDto from(BurialLocation location) {
        return new BurialLocationDto(
                location.getId(),
                location.getLatitude(),
                location.getLongitude(),
                location.isExact(),
                location.getDescription(),
                location.getPerson().getId(),
                location.getPerson().getLastName() + " "
                        + location.getPerson().getFirstName()
        );
    }

    public void applyTo(BurialLocation location) {
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setExact(exact);
        location.setDescription(description);
    }
}