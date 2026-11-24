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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        locationRepository.save(location);
        Set<Person> persons = getPersons(dto.personIds());
        for (Person person : persons) {
            person.setBurialLocation(location);
        }
        location.setPersons(persons);
        personRepository.saveAll(persons);
    }

    @Transactional
    public void update(Long id, BurialLocationDto dto) {
        BurialLocation location = getEntity(id);
        dto.applyTo(location);
        locationRepository.save(location);
    }

    @Transactional
    public void delete(Long id) {
        BurialLocation location = getEntity(id);
        for (Person person : location.getPersons()) {
            person.setBurialLocation(null);
        }
        personRepository.saveAll(location.getPersons());
        locationRepository.delete(location);
    }

    private BurialLocation getEntity(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Место захоронения не найдено"));
    }

    private Set<Person> getPersons(List<Long> personIds) {
        if (personIds == null || personIds.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Выберите хотя бы одного человека");
        }
        Set<Long> ids = new HashSet<>(personIds);
        List<Person> persons = personRepository.findAllById(ids);
        if (persons.size() != ids.size()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Один или несколько человек не найдены");
        }
        return new HashSet<>(persons);
    }
}