package com.siriusxi.tua.infra.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.io.Serializable;
import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

/**
 * Class to config the workflow Kafka send & listener message.
 * <p>We need a ProducerFactory support generate producer instance,
 * and we will have a KafkaTemplate use the producer instant for sending message to the topic.</p>
 */

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {
    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Bean
    public ProducerFactory<String, Serializable> producerFactory() {
        return new DefaultKafkaProducerFactory<>(Map.of(
                BOOTSTRAP_SERVERS_CONFIG, this.bootstrapAddress,
                KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class,
                MAX_REQUEST_SIZE_CONFIG, "20971520"), new StringSerializer(), new JsonSerializer<>());
    }

    @Bean
    KafkaTemplate<String, Serializable> jsonKafkaTemplate(ProducerFactory<String, Serializable> jsonProducerFactory) {
        return new KafkaTemplate<>(jsonProducerFactory);
    }
}
