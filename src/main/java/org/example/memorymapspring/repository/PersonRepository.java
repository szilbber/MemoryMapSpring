package org.example.memorymapspring.repository;

import org.example.memorymapspring.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}