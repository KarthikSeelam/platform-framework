package com.ican.cortex.platform.integration.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {

    @Autowired
    private CustomICanKafkaConsumer customKafkaConsumer;

    @GetMapping("/start-consuming")
    public String startConsuming() {
        try {
            customKafkaConsumer.startConsuming();
            return "Kafka consumer started!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
