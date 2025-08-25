package com.ican.cortex.platform.nhcx.messaging.service;

import com.ican.cortex.platform.nhcx.messaging.dto.NhcxEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NhcxKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.nhcx-events}")
    private String eventsTopic;

    @Value("${kafka.topic.nhcx-audit}")
    private String auditTopic;

    public void sendEvent(NhcxEvent event) {
        log.info("Sending event to topic {}: {}", eventsTopic, event);
        try {
            kafkaTemplate.send(eventsTopic, event.getCorrelationId(), event);
        } catch (Exception e) {
            log.error("Failed to send event to topic {}: {}", eventsTopic, event, e);
        }
    }

    public void sendAuditEvent(NhcxEvent event) {
        log.info("Sending audit event to topic {}: {}", auditTopic, event);
        try {
            kafkaTemplate.send(auditTopic, event.getCorrelationId(), event);
        } catch (Exception e) {
            log.error("Failed to send audit event to topic {}: {}", auditTopic, event, e);
        }
    }
}
