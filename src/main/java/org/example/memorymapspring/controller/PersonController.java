package org.example.memorymapspring.controller;

import jakarta.validation.Valid;
import org.example.memorymapspring.dto.PersonDto;
import org.example.memorymapspring.service.PersonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("persons", personService.findAll());
        model.addAttribute("person", PersonDto.empty());
        return "persons";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("person", personService.findById(id));
        model.addAttribute("id", id);
        return "person-edit";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("person") PersonDto person,
                         BindingResult result,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("persons", personService.findAll());
            return "persons";
        }
        personService.create(person);
        return "redirect:/persons";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("person") PersonDto person,
                         BindingResult result,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("id", id);
            return "person-edit";
        }
        personService.update(id, person);
        return "redirect:/persons";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        personService.delete(id);
        return "redirect:/persons";
    }
}