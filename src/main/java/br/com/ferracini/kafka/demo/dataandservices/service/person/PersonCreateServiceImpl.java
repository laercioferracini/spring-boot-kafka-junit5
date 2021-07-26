package br.com.ferracini.kafka.demo.dataandservices.service.person;

import br.com.ferracini.kafka.demo.dataandservices.converters.DTOConverters;
import br.com.ferracini.kafka.demo.dataandservices.dto.PersonDTO;
import br.com.ferracini.kafka.demo.dataandservices.entity.Person;
import br.com.ferracini.kafka.demo.dataandservices.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonCreateServiceImpl implements PersonCreateService {

    private final PersonValidateServiceImpl personValidateServiceImpl;
    private final PersonRepository personRepository;

    @Override
    @Transactional
    public List<PersonDTO> createFamily(Iterable<String> firstNames, String lastName){
        final var people = new ArrayList<PersonDTO>();
        firstNames.forEach(firstName -> people.add(createPerson(firstName, lastName)));
        return people;
    }

    @Override
    @Transactional
    public PersonDTO createPerson(String firstName, String lastName){
        personValidateServiceImpl.checkUserCreation(firstName, lastName);
        final var createdPerson = personRepository.saveAndFlush(new Person(firstName, lastName));
        return DTOConverters.toPersonDTO(createdPerson);
    }

}
