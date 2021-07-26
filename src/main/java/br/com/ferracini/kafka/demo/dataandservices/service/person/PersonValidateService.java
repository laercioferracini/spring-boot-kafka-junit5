package br.com.ferracini.kafka.demo.dataandservices.service.person;

import org.springframework.transaction.annotation.Transactional;

public interface PersonValidateService {
    @Transactional(readOnly = true)
    void checkUserCreation(String firstName, String lastName);
}
