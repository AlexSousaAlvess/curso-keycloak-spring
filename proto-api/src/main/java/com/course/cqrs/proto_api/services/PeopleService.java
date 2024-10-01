package com.course.cqrs.proto_api.services;


import com.course.cqrs.proto_api.models.Person;

import java.util.List;
import java.util.Optional;

public interface PeopleService {
    List<Person> getPeople();

    Optional<Person> getPerson(String id);

    Person createPerson(Person person);

    Person updatePerson(String id, Person person);

    void deletePerson(String id);

    List<Person> getPeopleByName(String name);

    void generatePeople(Integer quantity);
}
