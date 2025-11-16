package com.gabagool.gabagool.app.service;

import com.gabagool.gabagool.app.constants.Role;
import com.gabagool.gabagool.app.entity.Person;
import com.gabagool.gabagool.app.repository.PersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class PersonDetailsServiceTest {
    @Mock
    PersonRepository personRepository;

    @InjectMocks
    PersonDetailsService personDetailsService;

    @Test
    void testLoaderUserByUsernameSuccess() {
        String email = "test@test.com";
        String password = "pw";
        Role role = Role.USER;
        Person person = new Person("fst", "lst", email, password, role);
        Mockito.when(personRepository.findByEmail(email)).thenReturn(Optional.of(person));

        UserDetails result = personDetailsService.loadUserByUsername(email);

        verify(personRepository).findByEmail(email);
        assertEquals(email, result.getUsername());
        assertEquals(password, result.getPassword());
        assertEquals("ROLE_" + role, result.getAuthorities().toArray(new GrantedAuthority[0])[0].getAuthority());
        assertEquals(1, result.getAuthorities().size());
    }

}