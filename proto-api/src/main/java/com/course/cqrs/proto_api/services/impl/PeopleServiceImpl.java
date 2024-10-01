package com.course.cqrs.proto_api.services.impl;

import com.course.cqrs.proto_api.models.Person;
import com.course.cqrs.proto_api.services.PeopleService;
import com.github.javafaker.Faker;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PeopleServiceImpl implements PeopleService {

    private final List<Person> listOfPeople = new ArrayList<>();

    private final Faker faker = new Faker();

    @Override
    public List<Person> getPeople() {
        return listOfPeople;
    }

    @Override
    public Optional<Person> getPerson(String id) {
        return Optional.empty();
    }

    public Optional<Person> getPersonById(String id){
        return listOfPeople
                .stream()
                .filter(person -> person.getId().equals(id))
                .findFirst();
    }

    @Override
    public Person createPerson(Person person) {
        Person newPerson = Person.builder()
                .id(UUID.randomUUID().toString())
                .fullName(person.getFullName())
                .age(person.getAge())
                .birthDate(person.getBirthDate())
                .build();
        listOfPeople.add(newPerson);
        return newPerson;
    }

    @Override
    public Person updatePerson(String id, Person person) {
        Optional<Person> personToUpdate = getPersonById(id);
        if(personToUpdate.isEmpty()){
            throw new RuntimeException("Person not found");
        }
        listOfPeople.remove(personToUpdate.get());
        Person updatedPerson = changePerson(id, person);
        listOfPeople.add(updatedPerson);
        return updatedPerson;
    }

    private Person changePerson(String personId, Person person){
        return Person.builder()
                .id(personId)
                .fullName(person.getFullName())
                .birthDate(person.getBirthDate())
                .age(person.getAge())
                .build();
    }

    @Override
    public void deletePerson(String id) {
        Person findedPerson = getPersonById(id)
                .orElseThrow(()->new NoSuchElementException("Element not found"));
        listOfPeople.remove(findedPerson);
    }

    @Override
    public List<Person> getPeopleByName(String name) {
        return listOfPeople
                .stream()
                .filter(person -> person
                        .getFullName()
                        .toLowerCase()
                        .contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public void generatePeople(Integer quantity) {
        if(quantity <= 0) return;
        listOfPeople.clear();
        for(int i = 0; i < quantity; i++){
            Person person = Person.builder()
                    .id(UUID.randomUUID().toString())
                    .fullName(faker.name().fullName())
                    .birthDate(faker.date().birthday())
                    .age(faker.number().numberBetween(1,100))
                    .build();
            listOfPeople.add(person);
        }
    }
}
