package org.example.memorymapspring.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.example.memorymapspring.entity.BurialLocation;
import org.example.memorymapspring.entity.Person;

import java.util.List;

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

        List<Long> personIds,

        List<String> personNames
) {

    public BurialLocationDto {
        exact = Boolean.TRUE.equals(exact);
    }

    public static BurialLocationDto empty() {
        return new BurialLocationDto(null, null, null, false, null, List.of(), List.of());
    }

    public static BurialLocationDto from(BurialLocation location) {
        return new BurialLocationDto(
                location.getId(),
                location.getLatitude(),
                location.getLongitude(),
                location.isExact(),
                location.getDescription(),
                location.getPersons().stream()
                        .map(Person::getId)
                        .toList(),
                location.getPersons().stream()
                        .map(person -> person.getLastName() + " " + person.getFirstName())
                        .toList()
        );
    }

    public void applyTo(BurialLocation location) {
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setExact(exact);
        location.setDescription(description);
    }
}