package org.example.memorymapspring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "burial_locations")
public class BurialLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "is_exact", nullable = false)
    private boolean exact;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "burialLocation", fetch = FetchType.LAZY)
    private Set<Person> persons = new HashSet<>();
}