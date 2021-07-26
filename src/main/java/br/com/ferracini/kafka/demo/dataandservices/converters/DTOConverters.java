package br.com.ferracini.kafka.demo.dataandservices.converters;

import br.com.ferracini.kafka.demo.dataandservices.dto.PersonDTO;
import br.com.ferracini.kafka.demo.dataandservices.entity.Person;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DTOConverters {
    public static PersonDTO toPersonDTO(Person person) {
        return new PersonDTO(person.getId(),
                person.getFirstName(),
                person.getLastName(),
                person.getDateCreated()
        );
    }
}
