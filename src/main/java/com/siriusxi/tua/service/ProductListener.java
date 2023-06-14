package com.siriusxi.tua.service;

import com.siriusxi.tua.api.model.request.Product;
import com.siriusxi.tua.domain.ProductMessage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static java.time.Duration.between;
import static java.time.Instant.now;

@Slf4j
@Service
public class ProductListener {

    private final Map<String, OrderTracker> orderCountMap = new HashMap<>();


    private final int THRESHOLD;
    private final int ALERT_TIME_WINDOW; // in seconds

    public ProductListener(@Value(value = "${tua.business.threshold}") int threshold,
                           @Value("${tua.business.alert_time_window}") int alertTimeWindow) {
        this.THRESHOLD = threshold;
        this.ALERT_TIME_WINDOW = alertTimeWindow;
    }

    @KafkaListener(topics = "products", containerFactory = "kafkaListenerContainerFactory")
    public void newProductListener(ProductMessage product) {
        log.info("[ProductListener] Get request from product topic [{}] with action {}",
                product, product.action());

        var prod = product.product();
        checkProductCountReachThreshold(prod);
    }

    private void checkProductCountReachThreshold(Product prod) {

        // Check if the product name already exists in the order count map
        if (this.orderCountMap.containsKey(prod.name())) {
            // Increment the order count for the product
            OrderTracker orderTracker = this.orderCountMap.get(prod.name());
            orderTracker.incrementOrderCount();

            // Check if the order count exceeds the threshold within the time window
            if (orderTracker.getOrderCount() > this.THRESHOLD && isWithinTimeWindow(orderTracker)) {
                // Trigger the alert (send an email or print an error message)
                triggerAlert(prod.name(), orderTracker);
            }
        } else {
            // Add the product to the order count map with an initial count of 1
            this.orderCountMap.put(prod.name(), new OrderTracker());
        }
    }

    private boolean isWithinTimeWindow(OrderTracker orderTracker) {
        Instant lastOrderTime = orderTracker.getLastOrderTime();
        Instant currentTime = now();
        Duration timeElapsed = between(lastOrderTime, currentTime);

        // Check if the time elapsed is within the alert time window
        return timeElapsed.getSeconds() <= this.ALERT_TIME_WINDOW;
    }

    private void triggerAlert(String productName, OrderTracker orderTracker) {
        // Implement the logic to trigger the alert
        // For example, you can send an email using JavaMail or print an error message to the console
        log.error("""
                        ALERT!: Product '{}' has been ordered more than \
                        threshold {}; {} times within the last {} seconds!""",
                productName,
                this.THRESHOLD,
                orderTracker.getOrderCount(),
                this.ALERT_TIME_WINDOW);
    }

    @Getter
    private static class OrderTracker {
        private int orderCount;
        private Instant lastOrderTime;

        public OrderTracker() {
            this.orderCount = 1;
            this.lastOrderTime = now();
        }

        public void incrementOrderCount() {
            this.orderCount++;
            this.lastOrderTime = now();
        }
    }
}
