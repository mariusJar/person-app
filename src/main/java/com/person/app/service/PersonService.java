package com.person.app.service;

import com.person.app.model.Person;
import com.person.app.model.PersonDto;
import com.person.app.model.PersonMapper;
import com.person.app.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class PersonService {

    private PersonRepository personRepository;
    private PersonMapper personMapper;

    public PersonService(final PersonRepository personRepository,
                             final PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    public List<PersonDto> getPersons() {
        return personRepository
                .findAll()
                .stream()
                .map(personList -> personMapper.domainToDto(personList)).collect(toList());
    }

    public Long countPersons() {
        return personRepository.count();
    }


    public PersonDto getPerson(Long id) {
        Optional<Person> personOptional = personRepository.findById(id);

        if (!personOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Create Request");
        }

        log.debug("Retrieving person object: " + id);
        return personMapper.domainToDto(personOptional.get());
    }

    @Transactional
    public PersonDto addPerson(PersonDto personDto) {
        Optional<Person> personOptional = personRepository.findById(personDto.getId());

        if (personOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Create Request");
        }

        log.debug("Creating person object: " + personDto.getFirstName());
        return save(personDto);
    }

    @Transactional
    public PersonDto updatePerson(PersonDto personDto) {
        if (personDto.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id is Required");
        }

        Optional<Person> personOptional = personRepository.findById(personDto.getId());

        if (!personOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Update Request");
        }

        Person oldPerson = personOptional.get();

        if (StringUtils.isEmpty(personDto.getSurname())) {
            personDto.setSurname(oldPerson.getSurname());
        }

        if (StringUtils.isEmpty(personDto.getFirstName())) {
            personDto.setFirstName(oldPerson.getFirstName());
        }

        log.debug("Updating person object: " + personDto.getFirstName());
        return save(personDto);
    }

    @Transactional
    public boolean deletePerson(Long id) {
        Optional<Person> personOptional = personRepository.findById(id);

        if (!personOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Delete Request");
        }

        log.debug("Deleting person object: " + id);
        personRepository.deleteById(id);
        return true;
    }

    private PersonDto save(PersonDto personDto) {
        Person person = personRepository.saveAndFlush(personMapper.dtoToDomain(personDto));

        log.debug("Saving person object: " + personDto.getFirstName());
        return personMapper.domainToDto(person);
    }
}
