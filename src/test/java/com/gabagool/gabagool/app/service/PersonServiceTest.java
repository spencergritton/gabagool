package com.gabagool.gabagool.app.service;

import com.gabagool.gabagool.app.constants.Role;
import com.gabagool.gabagool.app.dto.request.RegisterRequest;
import com.gabagool.gabagool.app.entity.Person;
import com.gabagool.gabagool.app.repository.PersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class PersonServiceTest {
    @Mock
    private PersonRepository personRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PersonService personService;

    @Test
    void testCreatePersonSuccess() {
        String encodedPassword = "encodedPassword";
        RegisterRequest request = new RegisterRequest(
                "first", "last", "test@test.com",
                "password", "password");
        when(passwordEncoder.encode(request.getPassword())).thenReturn(encodedPassword);

        Person result = personService.createPerson(request);

        assertEquals(request.getFirstName(), result.getFirstName());
        assertEquals(request.getLastName(), result.getLastName());
        assertEquals(request.getEmail(), result.getEmail());
        assertEquals(encodedPassword, result.getPassword());
        assertEquals(Role.USER, result.getRole());
        verify(passwordEncoder).encode(request.getPassword());
        verify(personRepository).save(any(Person.class));
    }

    @Test
    void testCreatePersonFailure() {
        String encodedPassword = "encodedPassword";
        RegisterRequest request = new RegisterRequest(
                "first", "last", "test@test.com",
                "password", "password");
        when(passwordEncoder.encode(request.getPassword())).thenReturn(encodedPassword);
        when(personRepository.save(any())).thenThrow(new RuntimeException("Error"));

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> personService.createPerson(request));

        assertEquals("Error", exception.getMessage());
        verify(passwordEncoder).encode(request.getPassword());
        verify(personRepository).save(any(Person.class));
    }

    @Test
    void testFindPersonByEmailSuccess() {
        String email = "test@test.com";
        Person person = new Person("fst", "lst", email,"pass", Role.USER);
        when(personRepository.findByEmail(email)).thenReturn(Optional.of(person));

        Person result = personService.findPersonByEmail(email);

        assertEquals(person, result);
        verify(personRepository).findByEmail(email);
    }

    @Test
    void testFindPersonByEmailFailure() {
        String email = "test@test.com";
        when(personRepository.findByEmail(email)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> personService.findPersonByEmail(email));

        assertEquals("Unable to user with that email or password.", exception.getMessage());
        verify(personRepository).findByEmail(email);
    }
}