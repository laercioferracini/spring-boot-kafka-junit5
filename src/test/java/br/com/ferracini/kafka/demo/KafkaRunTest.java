package br.com.ferracini.kafka.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test-containers")
public class KafkaRunTest {

    @Container
    public KafkaContainer container = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.0"));

    @Test
    void sendMessage() {
        assertTrue(container.isRunning());
    }
}
