package com.siriusxi.tua.infra.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.Map;

/**
 * A class to create Kafka topic programmatically.
 * Using this configuration will allow you to easily create topics programmatically with <code>AdminClient</code> in Spring-Kafka.
 * This class will create a new topic <code>product</code> with <code>KafkaAdmin</code>.
 * <p>
 * After application start the configuration will be run automatically,
 * and if the top don’t exist the Kafka Amin will create a new one.
 * </p>
 *
 * @author Mohamed Taman
 */
@Configuration
public class KafkaTopicConfig {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value(value = "${kafka.topic.name}")
    private String topicName;
    @Value(value = "${kafka.topic.partitions}")
    private int partitions;

    @Value(value = "${kafka.topic.replicas}")
    private short replicas;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        return new KafkaAdmin(Map.of(
                AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,
                this.bootstrapAddress));
    }

    @Bean
    public NewTopic createNewProductTopic() {
        return new NewTopic(this.topicName, this.partitions, this.replicas);
    }
}