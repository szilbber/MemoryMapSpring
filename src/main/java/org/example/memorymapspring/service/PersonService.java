package org.example.memorymapspring.service;

import lombok.RequiredArgsConstructor;
import org.example.memorymapspring.dto.PersonDto;
import org.example.memorymapspring.entity.Person;
import org.example.memorymapspring.repository.PersonRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    @Transactional(readOnly = true)
    public List<PersonDto> findAll() {
        return personRepository.findAll(Sort.by("id")).stream()
                .map(PersonDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public PersonDto findById(Long id) {
        return PersonDto.from(getEntity(id));
    }

    @Transactional
    public void create(PersonDto dto) {
        Person person = new Person();
        dto.applyTo(person);
        personRepository.save(person);
    }

    @Transactional
    public void update(Long id, PersonDto dto) {
        Person person = getEntity(id);
        dto.applyTo(person);
        personRepository.save(person);
    }

    @Transactional
    public void delete(Long id) {
        personRepository.delete(getEntity(id));
    }

    private Person getEntity(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Человек не найден"));
    }
}