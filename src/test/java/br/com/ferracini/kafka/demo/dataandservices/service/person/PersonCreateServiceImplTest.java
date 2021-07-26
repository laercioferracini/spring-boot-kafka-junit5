package br.com.ferracini.kafka.demo.dataandservices.service.person;

import br.com.ferracini.kafka.demo.dataandservices.exception.ValidationFailedException;
import br.com.ferracini.kafka.demo.dataandservices.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
@ActiveProfiles("test-containers")
class PersonCreateServiceImplTest {

    @Autowired
    private PersonRepository personRepository;

    @MockBean
    private PersonValidateServiceImpl personValidateService;

    @Autowired
    private PersonCreateServiceImpl personCreateService;

    @BeforeEach
    void init() {
        personRepository.deleteAll();
    }

    @Test
    void shouldCreateOnePerson() {
        final var people = personCreateService.createFamily(
                List.of("Simon"),
                "Kirekov"
        );
        assertEquals(1, people.size());
        final var person = people.get(0);
        assertEquals("Simon", person.getFirstName());
        assertEquals("Kirekov", person.getLastName());
        assertTrue(person.getDateCreated().isBefore(ZonedDateTime.now()));
    }

    @Test
    void shouldRollbackIfAnyUserIsNotValidated() {
        doThrow(new ValidationFailedException(""))
                .when(personValidateService)
                .checkUserCreation("John", "Brown");

        assertThrows(ValidationFailedException.class, () -> personCreateService.createFamily(
                List.of("Matilda", "Vasya", "John"),
                "Brown"
        ));
        assertEquals(0, personRepository.count());
    }

}