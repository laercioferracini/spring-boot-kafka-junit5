package br.com.ferracini.kafka.demo.dataandservices.service.person;

import br.com.ferracini.kafka.demo.dataandservices.exception.ValidationFailedException;
import br.com.ferracini.kafka.demo.dataandservices.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PersonValidateServiceImpl implements PersonValidateService {

    private final PersonRepository personRepository;

    @Override
    @Transactional(readOnly = true)
    public void checkUserCreation(String firstName, String lastName){
        if (personRepository.existsByFirstNameAndLastName(firstName, lastName)) {
            throw new ValidationFailedException("Such person already exists");
        }
    }
}
