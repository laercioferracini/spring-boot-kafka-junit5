package br.com.ferracini.kafka.demo.dataandservices.service.person;

import br.com.ferracini.kafka.demo.dataandservices.entity.Person;
import br.com.ferracini.kafka.demo.dataandservices.exception.ValidationFailedException;
import br.com.ferracini.kafka.demo.dataandservices.repository.PersonRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PersonCreateServiceImplMockingTest {


    private final PersonValidateServiceImpl personValidateService = mock(PersonValidateServiceImpl.class);
    private final PersonRepository personRepository = mock(PersonRepository.class);
    private final PersonCreateServiceImpl service = new PersonCreateServiceImpl(personValidateService, personRepository);

    @Test
    void shouldFailUserCreation() {
        final var firstName = "Yakov";
        final var lastName = "ov";
        doThrow(new ValidationFailedException(""))
                .when(personValidateService)
                .checkUserCreation(firstName, lastName);

        assertThrows(ValidationFailedException.class,
                () -> service.createPerson(firstName, lastName));
    }

    @Test
    void shouldCreateNewUser() {

        final var firstName = "Yakov";
        final var lastName = "ov";

        when(personRepository.saveAndFlush(any()))
                .thenAnswer(invocation -> {
                    var p = invocation.getArgument(0, Person.class);
                    assert Objects.equals(p.getFirstName(), firstName);
                    assert Objects.equals(p.getLastName(), lastName);

                    return p.setId(1L);
                });
        final var personDTO = service.createPerson(firstName, lastName);

        assertEquals(firstName, personDTO.getFirstName());
        assertEquals(lastName, personDTO.getLastName());
        assertNull(personDTO.getDateCreated());
        assertEquals(1L, personDTO.getId());
    }

    @Test
    void shouldCreateFamily() {
        final var firstNames = List.of("Ya'kov", "Abraham", "Sarah");
        final var lastName = "Israel";
        final var idHolder = new AtomicLong(0);

        when(personRepository.saveAndFlush(any()))
                .thenAnswer(invocation -> {
                    var person = invocation.getArgument(0, Person.class);
                    assert firstNames.contains(person.getFirstName());
                    assert Objects.equals(person.getLastName(), lastName);
                    return person.setId(idHolder.incrementAndGet());
                });
        final var people = service.createFamily(firstNames, lastName);

        for (int i = 0; i < people.size(); i++) {
            final var personDTO = people.get(i);
            assertEquals(firstNames.get(i), personDTO.getFirstName());
            assertEquals(lastName, personDTO.getLastName());
            assertEquals(i + 1, personDTO.getId());
        }

        verify(personValidateService, times(3)).checkUserCreation(any(), any());
        verify(personRepository, times(3)).saveAndFlush(any());
    }
}
