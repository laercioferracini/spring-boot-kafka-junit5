package br.com.ferracini.dataandservices.service.person;

import br.com.ferracini.dataandservices.dto.PersonDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PersonCreateService {
    @Transactional
    List<PersonDTO> createFamily(Iterable<String> firstName, String lastName);

    @Transactional
    PersonDTO createPerson(String firstName, String lastName);
}
