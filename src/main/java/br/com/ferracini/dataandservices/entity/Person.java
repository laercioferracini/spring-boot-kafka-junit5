package br.com.ferracini.dataandservices.entity;

import lombok.Getter;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "person")
@Getter
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date_created")
    private ZonedDateTime dateCreated;

    @PrePersist
    @PreUpdate
    void setCurrentTimeAsDateCreated() {
        dateCreated = ZonedDateTime.now();
    }

    public Person setId(Long id) {
        this.id = id;
        return this;
    }

    public Person setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public Person setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Person(String firstName, String lastName) {
        this.firstName =firstName;
        this.lastName = lastName;
    }
}
