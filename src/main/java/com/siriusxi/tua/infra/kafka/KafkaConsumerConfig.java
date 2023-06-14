package com.siriusxi.tua.infra.kafka;

import com.siriusxi.tua.domain.ProductMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

@Slf4j
@Configuration
public class KafkaConsumerConfig {
    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    public ConsumerFactory<String, ProductMessage> consumerFactory() {

        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setIdClassMapping(new HashMap<>());
        typeMapper.addTrustedPackages("*");

        JsonDeserializer<ProductMessage> jsonDeserializer = new JsonDeserializer<>(ProductMessage.class);
        jsonDeserializer.setTypeMapper(typeMapper);
        jsonDeserializer.setUseTypeMapperForKey(true);

        return new DefaultKafkaConsumerFactory<>(Map.of(
                BOOTSTRAP_SERVERS_CONFIG, this.bootstrapAddress,
                GROUP_ID_CONFIG, "consuming"),
                new StringDeserializer(), jsonDeserializer);
    }

    public Map<String, Object> consumerConfigs() {
        return Map.of(BOOTSTRAP_SERVERS_CONFIG, this.bootstrapAddress,
                KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                GROUP_ID_CONFIG, "consuming");
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProductMessage> kafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, ProductMessage>();
        factory.setConsumerFactory(consumerFactory());
        log.info("Configure concurrent consumer Kafka");
        return factory;
    }
}
