package org.example.memorymapspring.controller;

import jakarta.validation.Valid;
import org.example.memorymapspring.dto.BurialLocationDto;
import org.example.memorymapspring.service.BurialLocationService;
import org.example.memorymapspring.service.PersonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/burial-locations")
public class BurialLocationController {

    private final BurialLocationService burialLocationService;
    private final PersonService personService;

    public BurialLocationController(BurialLocationService burialLocationService,
                                    PersonService personService) {
        this.burialLocationService = burialLocationService;
        this.personService = personService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("locations", burialLocationService.findAll());
        model.addAttribute("persons", personService.findAll());   // вот эта
        return "burial-locations";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("location", BurialLocationDto.empty());
        model.addAttribute("persons", personService.findAll());
        return "burial-location-edit";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("location", burialLocationService.findById(id));
        model.addAttribute("id", id);
        model.addAttribute("persons", personService.findAll());
        return "burial-location-edit";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("location") BurialLocationDto location,
                         BindingResult result,
                         Model model) {

        if (!result.hasFieldErrors("personId")
                && burialLocationService.existsForPerson(location.personId())) {
            result.rejectValue("personId", "duplicate",
                    "У этого человека уже есть место захоронения. Выберите другого человека.");
        }

        if (result.hasErrors()) {
            model.addAttribute("persons", personService.findAll());
            return "burial-location-edit";
        }

        burialLocationService.create(location);
        return "redirect:/burial-locations";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("location") BurialLocationDto location,
                         BindingResult result,
                         Model model) {

        if (!result.hasFieldErrors("personId")
                && burialLocationService.existsForPersonExceptLocation(
                location.personId(), id)) {
            result.rejectValue("personId", "duplicate",
                    "У выбранного человека уже есть другое место захоронения. Выберите другого человека.");
        }

        if (result.hasErrors()) {
            model.addAttribute("id", id);
            model.addAttribute("persons", personService.findAll());
            return "burial-location-edit";
        }

        burialLocationService.update(id, location);
        return "redirect:/burial-locations";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        burialLocationService.delete(id);
        return "redirect:/burial-locations";
    }
}