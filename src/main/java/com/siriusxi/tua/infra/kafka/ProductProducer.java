package com.siriusxi.tua.infra.kafka;

import com.siriusxi.tua.domain.ProductMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ProductProducer {

    @Value(value = "${kafka.topic.name}")
    private String productTopic;

    private KafkaTemplate<String, Serializable> kafkaTemplate;

    @Autowired
    public ProductProducer(KafkaTemplate<String, Serializable> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(ProductMessage message) {
        this.kafkaTemplate.send(this.productTopic, message).
                whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Successfully sent message = [{}] with offset = [{}]",
                                message, result.getRecordMetadata().offset());
                    } else {
                        log.error("Unable to send message= [{}] due to : {}",
                                message, ex.getMessage());
                    }
                });
    }

}
