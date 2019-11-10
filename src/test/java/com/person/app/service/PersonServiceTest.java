package com.person.app.service;

import com.person.app.model.Person;
import com.person.app.model.PersonDto;
import com.person.app.model.PersonMapper;
import com.person.app.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Person Service Test Cases")
class PersonServiceTest {

    private final static String NAME = "Test";

    private PersonRepository personRepository;
    private PersonMapper personMapper;

    private Person personObject = Person.builder().id(1L).firstName(NAME).build();
    private PersonDto personDto = PersonDto.builder().id(1L).firstName(NAME).build();

    @InjectMocks
    private PersonService personService;

    @BeforeEach
    public void initMocks() {
        personService = new PersonService(personRepository, personMapper);
        personMapper = mock(PersonMapper.class);
        personRepository = mock(PersonRepository.class);
        MockitoAnnotations.initMocks(this);
    }


    @DisplayName("Should return list with 5 PersonDto objects successfully")
    @Test
    void getPersonsSuccess() {
        List<Person> personList = new ArrayList<>();

        personList.add(personObject);
        personList.add(personObject);
        personList.add(personObject);
        personList.add(personObject);
        personList.add(personObject);

        List<PersonDto> personDtos = new ArrayList<>();

        personDtos.add(personDto);
        personDtos.add(personDto);
        personDtos.add(personDto);
        personDtos.add(personDto);
        personDtos.add(personDto);

        when(personRepository.findAll()).thenReturn(personList);
        when(personMapper.domainToDto(personObject)).thenReturn(personDto);

        List<PersonDto> personDtoList = personService.getPersons();

        assertThat(personDtoList).isEqualTo(personDtos);
    }

    @DisplayName("Should return an empty list")
    @Test
    void getPersonsEmpty() {
        List<Person> personList = new ArrayList<>();
        List<PersonDto> personDtos = new ArrayList<>();

        when(personRepository.findAll()).thenReturn(personList);

        List<PersonDto> personDtoList = personService.getPersons();

        assertThat(personDtoList).isEqualTo(personDtos);
    }

    @DisplayName("Should return PersonDto object")
    @Test
    void getPersonSuccess() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(personObject));
        when(personMapper.domainToDto(personObject)).thenReturn(personDto);

        PersonDto newPersonDto = personService.getPerson(1L);

        assertThat(personDto).isEqualTo(newPersonDto);
    }

    @DisplayName("Should throw Bad Request exception when record not found")
    @Test
    void getPersonResponseStatusException() {
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> personService.getPerson(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("400 BAD_REQUEST \"Bad Create Request\"");
    }

    @DisplayName("Should create Person object successfully")
    @Test
    void addPersonSuccess() {
        when(personRepository.findById(1L)).thenReturn(Optional.empty());
        when(personRepository.saveAndFlush(personObject)).thenReturn(personObject);
        when(personMapper.domainToDto(personObject)).thenReturn(personDto);
        when(personMapper.dtoToDomain(personDto)).thenReturn(personObject);

        PersonDto newPersonDto = personService.addPerson(personDto);

        assertThat(personDto).isEqualTo(newPersonDto);
    }

    @DisplayName("Should throw Bad Request exception when Person already exists")
    @Test
    void addPersonResponseStatusException() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(personObject));

        assertThatThrownBy(() -> personService.addPerson(personDto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("400 BAD_REQUEST \"Bad Create Request\"");
    }

    @DisplayName("Should update Person successfully")
    @Test
    void updatePersonSuccess() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(personObject));
        when(personRepository.saveAndFlush(personObject)).thenReturn(personObject);
        when(personMapper.domainToDto(personObject)).thenReturn(personDto);
        when(personMapper.dtoToDomain(personDto)).thenReturn(personObject);

        PersonDto newPersonDto = personService.updatePerson(personDto);

        assertThat(personDto).isEqualTo(newPersonDto);
    }

    @DisplayName("Should throw Bad Request exception when Person does not exists while updating")
    @Test
    void updatePersonResponseStatusException() {
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> personService.updatePerson(personDto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("400 BAD_REQUEST \"Bad Update Request\"");
    }

    @DisplayName("Should delete Person successfully")
    @Test
    void deletePersonSuccess() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(personObject));
//        when(personRepository.deleteById(1L));
        assertThat(personService.deletePerson(1L)).isTrue();
    }
}