package br.com.ferracini.kafka.demo.dataandservices.repository;


import br.com.ferracini.kafka.demo.dataandservices.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface PersonRepository extends JpaRepository<Person, Long> {
    boolean existsByFirstNameAndLastName(String firstName, String lastName);

    @Query("select distinct p.lastName from Person p")
    Set<String> findAllLastNames();
}
