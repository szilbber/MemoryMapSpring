package org.example.memorymapspring.repository;

import org.example.memorymapspring.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findByBurialLocationIsNull();

}