package com.ican.cortex.platform.nhcx.hcx.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class HcxHeaderGenerator {

    @Value("${hcx.sender.code}")
    private String senderCode;

    @Value("${hcx.recipient.code}")
    private String recipientCode;

    public Map<String, String> generateHeaders(String correlationId, String apiCallId, String workflowId) {
        Map<String, String> headers = new HashMap<>();
        headers.put("x-hcx-sender_code", senderCode);
        headers.put("x-hcx-recipient_code", recipientCode);
        headers.put("x-hcx-api_call_id", apiCallId);
        headers.put("x-hcx-correlation_id", correlationId);
        headers.put("x-hcx-timestamp", String.valueOf(Instant.now().toEpochMilli()));
        if (workflowId != null) {
            headers.put("x-hcx-workflow_id", workflowId);
        }
        // As per spec, this is for redirect scenarios.
        // headers.put("x-hcx-status", "request.redirected");
        return headers;
    }
}
