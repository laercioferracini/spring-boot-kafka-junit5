package br.com.ferracini.dataandservices.dto;

import lombok.Value;

import java.time.ZonedDateTime;

@Value
public class PersonDTO {
    Long id;
    String firstName;
    String lastName;
    ZonedDateTime dateCreated;
}
