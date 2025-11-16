package com.gabagool.gabagool.app.controller;

import com.gabagool.gabagool.app.entity.Person;
import com.gabagool.gabagool.app.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PublicControllerIntegrationTest {
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "passPass123";
    private static final String CONFIRM_PASSWORD = "passPass123";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonService personService;

    @Test
    void testRegisterEndpointSuccess() throws Exception {
        mockMvc.perform(post("/register")
                .param("firstName", FIRST_NAME)
                .param("lastName", LAST_NAME)
                .param("email", EMAIL)
                .param("password", PASSWORD)
                .param("confirmPassword", CONFIRM_PASSWORD)
                .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("fragments/success"));

        Person person = personService.findPersonByEmail(EMAIL);
        assertEquals(FIRST_NAME, person.getFirstName());
        assertEquals(LAST_NAME, person.getLastName());
        assertEquals(EMAIL, person.getEmail());
    }

    @Test
    void testRegisterEndpointFailureNoCSRF() throws Exception {
        mockMvc.perform(post("/register")
                        .param("firstName", FIRST_NAME)
                        .param("lastName", LAST_NAME)
                        .param("email", EMAIL)
                        .param("password", PASSWORD)
                        .param("confirmPassword", CONFIRM_PASSWORD))
                .andExpect(status().isForbidden());

        assertThrows(Exception.class, () -> personService.findPersonByEmail(EMAIL));
    }
}
