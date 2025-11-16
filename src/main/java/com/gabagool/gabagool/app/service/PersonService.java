package com.gabagool.gabagool.app.service;

import com.gabagool.gabagool.app.dto.request.RegisterRequest;
import com.gabagool.gabagool.app.entity.Person;
import com.gabagool.gabagool.app.repository.PersonRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Person createPerson(RegisterRequest registerRequest) {
        Person person = new Person();
        person.setEmail(registerRequest.getEmail());
        person.setFirstName(registerRequest.getFirstName());
        person.setLastName(registerRequest.getLastName());
        person.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        personRepository.save(person);
        return person;
    }

    public Person findPersonByEmail(String email) {
        return personRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("Unable to user with that email or password.")
        );
    }
}
