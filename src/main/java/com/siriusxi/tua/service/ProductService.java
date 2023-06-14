package com.siriusxi.tua.service;

import com.siriusxi.tua.domain.ProductMessage;
import com.siriusxi.tua.infra.kafka.ProductProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductService {
    private final ProductProducer productProducer;

    @Autowired
    public ProductService(ProductProducer productProducer) {
        this.productProducer = productProducer;
    }

    public void sendMessage(ProductMessage message) {
        log.info("[ProductService] send product {} to topic.", message);
        this.productProducer.send(message);
    }

}
