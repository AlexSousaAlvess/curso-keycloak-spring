package com.course.cqrs.proto_api.controllers;

import com.course.cqrs.proto_api.models.Person;
import com.course.cqrs.proto_api.services.PeopleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;

    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Value("${api.version}")
    private String apiVersion;


    @GetMapping("/version")
    public String getApiVersion(){
        return apiVersion;
    }

    @GetMapping("/create-people/{quantity}")
    public String createPeople(@PathVariable("quantity") Integer quantity){
        peopleService.generatePeople(quantity);
        return String.format("Created %d people", quantity);
    }

    @GetMapping("/")
    public ResponseEntity<List<Person>> getPeople(){
        return ResponseEntity.ok(peopleService.getPeople());
    }

    @PostMapping("/create-person")
    public ResponseEntity<Person> createNewPerson(@RequestBody Person person){
        return ResponseEntity.ok(this.peopleService.createPerson(person));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable("id") String id){
        peopleService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable("id") String id, @RequestBody Person person){
        return ResponseEntity.ok(peopleService.updatePerson(id, person));
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<List<Person>> getPeopleByName(@PathVariable("name") String name){
        return ResponseEntity.ok(peopleService.getPeopleByName(name));
    }
}
