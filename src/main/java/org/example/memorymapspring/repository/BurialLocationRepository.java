package org.example.memorymapspring.repository;

import org.example.memorymapspring.entity.BurialLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BurialLocationRepository extends JpaRepository<BurialLocation, Long> {

    @Query("""
    SELECT DISTINCT b
    FROM BurialLocation b
    LEFT JOIN FETCH b.persons
    ORDER BY b.id
    """)
    List<BurialLocation> findAllWithPerson();
}