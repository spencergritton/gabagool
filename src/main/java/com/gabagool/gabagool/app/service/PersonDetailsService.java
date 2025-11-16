package com.gabagool.gabagool.app.service;

import com.gabagool.gabagool.app.entity.Person;
import com.gabagool.gabagool.app.repository.PersonRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PersonDetailsService implements UserDetailsService {
    private final PersonRepository personRepository;

    public PersonDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Person person = personRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(String.format("Unable to find username %s", email)));

        return User.builder()
                .username(email)
                .password(person.getPassword())
                .roles(String.valueOf(person.getRole()))
                .build();
    }
}
