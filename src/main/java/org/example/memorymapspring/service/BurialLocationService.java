package org.example.memorymapspring.service;

import lombok.RequiredArgsConstructor;
import org.example.memorymapspring.dto.BurialLocationDto;
import org.example.memorymapspring.entity.BurialLocation;
import org.example.memorymapspring.entity.Person;
import org.example.memorymapspring.repository.BurialLocationRepository;
import org.example.memorymapspring.repository.PersonRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BurialLocationService {

    private final BurialLocationRepository locationRepository;
    private final PersonRepository personRepository;

    @Transactional(readOnly = true)
    public List<BurialLocationDto> findAll() {
        return locationRepository.findAllWithPerson().stream()
                .map(BurialLocationDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public BurialLocationDto findById(Long id) {
        return BurialLocationDto.from(getEntity(id));
    }

    @Transactional
    public void create(BurialLocationDto dto) {
        BurialLocation location = new BurialLocation();
        dto.applyTo(location);
        location.setPerson(getPerson(dto.personId()));
        locationRepository.save(location);
    }

    @Transactional
    public void update(Long id, BurialLocationDto dto) {
        BurialLocation location = getEntity(id);
        dto.applyTo(location);
        location.setPerson(getPerson(dto.personId()));
        locationRepository.save(location);
    }

    @Transactional
    public void delete(Long id) {
        locationRepository.delete(getEntity(id));
    }

    @Transactional(readOnly = true)
    public boolean existsForPerson(Long personId) {
        return locationRepository.existsByPersonId(personId);
    }

    @Transactional(readOnly = true)
    public boolean existsForPersonExceptLocation(Long personId, Long locationId) {
        return locationRepository.existsByPersonIdAndIdNot(personId, locationId);
    }

    private BurialLocation getEntity(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Место захоронения не найдено"));
    }

    private Person getPerson(Long personId) {
        return personRepository.findById(personId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Человек не найден"));
    }
}