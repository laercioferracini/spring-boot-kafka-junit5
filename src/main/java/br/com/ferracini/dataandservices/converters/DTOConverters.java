package br.com.ferracini.dataandservices.converters;

import br.com.ferracini.dataandservices.dto.PersonDTO;
import br.com.ferracini.dataandservices.entity.Person;
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
