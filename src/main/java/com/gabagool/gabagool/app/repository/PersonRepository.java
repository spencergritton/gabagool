package com.gabagool.gabagool.app.repository;

import com.gabagool.gabagool.app.dto.entity.PersonLightDto;
import com.gabagool.gabagool.app.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PersonRepository extends JpaRepository<Person, UUID> {
    Optional<Person> findByEmail(String email);

    @Query("""
            select new com.gabagool.gabagool.app.dto.entity.PersonLightDto(p.email)
            from Person p
            where p.email = :email""")
    Optional<PersonLightDto> findPersonLightDtoByEmail(@Param("email") String email);
}
