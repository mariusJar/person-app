package com.person.app.controller;

import com.person.app.model.PersonDto;
import com.person.app.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/person")
public class PersonController {

    private PersonService personService;

    public PersonController(final PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/all")
    public List<PersonDto> getPersons() {
        return personService.getPersons();
    }

    @GetMapping("/count")
    public Long countPersons() {
        return personService.countPersons();
    }

    @GetMapping
    public PersonDto getPerson(
            @RequestParam(name = "id") @Valid @Min(1) Long id
    ) {
        return personService.getPerson(id);
    }

    @PostMapping(produces = { "application/json", "application/xml" }, consumes = {
            "application/json", "application/xml" })
    public PersonDto addPerson(
            @Validated @RequestBody PersonDto personDto
    ) {
        return personService.addPerson(personDto);
    }

    @PutMapping
    public PersonDto updatePerson(
            @Validated @RequestBody PersonDto personDto
    ) {
        return personService.updatePerson(personDto);
    }

    @DeleteMapping
    public boolean deletePerson(
            @RequestParam(name = "id") @Valid @Min(1) Long id
    ) {
        return personService.deletePerson(id);
    }
}
