package com.person.app.model;

import org.springframework.stereotype.Component;
import org.dozer.DozerBeanMapper;

@Component
public class PersonMapper {

    final static DozerBeanMapper mapper = new DozerBeanMapper();

    public Person dtoToDomain(PersonDto personDto) {
        if (personDto == null) {
            return null;
        }

        return mapper.map(personDto, Person.class);
    }

    public PersonDto domainToDto(Person person) {
        return mapper.map(person, PersonDto.class);
    }
}
