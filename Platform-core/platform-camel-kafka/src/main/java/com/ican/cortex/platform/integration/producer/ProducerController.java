package com.ican.cortex.platform.integration.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {

    @Autowired
    private CustomICanKafkaProducer customKafkaProducer;

    @PostMapping("/send-message")
    public String sendMessage(@RequestBody String jsonMessage) {
        try {
            customKafkaProducer.sendCustomMessage(jsonMessage);
            return "Message sent successfully!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
