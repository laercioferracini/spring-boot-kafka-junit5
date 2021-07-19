package br.com.ferracini.kafka.demo;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers

public class ContainerizedKafkaTest {
    public static final String MY_TOPIC = "my-topic";
    public static final int NUMBER_OF_MESSAGES = 100;

    @Container
    public KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.0"));

    @Test
    @DisplayName("kafka server should be running")
    void shouldBeRunningKafka() throws Exception {
        assertTrue(kafkaContainer.isRunning());
    }

    @Test
    @DisplayName("should send and receive records over kafka")
    void shouldSendAndReceiveMessages() throws Exception {
        var servers = kafkaContainer.getBootstrapServers();
        System.out.printf("servers: %s%n", servers);

        var props = new Properties();
        props.put("bootstrap.servers", servers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put("group.id", "group-1");
        props.put("auto.offset.reset", "earliest");

        var counter = new AtomicInteger(0);
        CountDownLatch waitABit = new CountDownLatch(1);

        new Thread(() -> {
            KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(props);
            kafkaConsumer.subscribe(Arrays.asList(MY_TOPIC));
            while (counter.get()<NUMBER_OF_MESSAGES) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(200);
                records.forEach(record -> {
                    System.out
                            .printf("%d # offset: %d, value = %s%n", counter.incrementAndGet(), record.offset(),
                                    record.value());
                });
            }
            waitABit.countDown();

        }).start();

        try (
                Producer<String, String> producer = new KafkaProducer<>(props)) {
            IntStream.range(0, NUMBER_OF_MESSAGES).forEach(i -> {
                var msg = String.format("my-message-%d", i);
                producer.send(new ProducerRecord<>(MY_TOPIC, msg));
                System.out.println("Sent:" + msg);
            });
        }

        waitABit.await();
        assertEquals(NUMBER_OF_MESSAGES, counter.get());
    }
}
