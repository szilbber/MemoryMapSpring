package org.example.memorymapspring.repository;

import org.example.memorymapspring.entity.BurialLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BurialLocationRepository extends JpaRepository<BurialLocation, Long> {

    boolean existsByPersonId(Long personId);

    boolean existsByPersonIdAndIdNot(Long personId, Long id);

    @Query("SELECT b FROM BurialLocation b JOIN FETCH b.person ORDER BY b.id")
    List<BurialLocation> findAllWithPerson();
}