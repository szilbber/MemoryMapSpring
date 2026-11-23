package org.example.memorymapspring.dto;

import jakarta.validation.constraints.NotBlank;
import org.example.memorymapspring.entity.Person;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record PersonDto(
        Long id,

        @NotBlank(message = "Введите имя")
        String firstName,

        @NotBlank(message = "Введите фамилию")
        String lastName,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate birthDate,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate deathDate,

        String description,

        String burialLocationDescription
) {

    public static PersonDto empty() {
        return new PersonDto(null, null, null, null, null, null, null);
    }

    public static PersonDto from(Person person) {
        return new PersonDto(
                person.getId(),
                person.getFirstName(),
                person.getLastName(),
                person.getBirthDate(),
                person.getDeathDate(),
                person.getDescription(),
                person.getBurialLocation() != null
                        ? person.getBurialLocation().getDescription()
                        : null
        );
    }

    public void applyTo(Person person) {
        person.setFirstName(firstName.trim());
        person.setLastName(lastName.trim());
        person.setBirthDate(birthDate);
        person.setDeathDate(deathDate);
        person.setDescription(description);
    }
}