package com.ican.cortex.platform.nhcx.hcx.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class HcxHeaderGeneratorTest {

    private HcxHeaderGenerator hcxHeaderGenerator;

    @BeforeEach
    void setUp() {
        hcxHeaderGenerator = new HcxHeaderGenerator();
        ReflectionTestUtils.setField(hcxHeaderGenerator, "senderCode", "test-sender-code");
        ReflectionTestUtils.setField(hcxHeaderGenerator, "recipientCode", "test-recipient-code");
    }

    @Test
    void testGenerateHeaders_AllFields_ShouldBePresent() {
        // Given
        String correlationId = "corr-12345";
        String apiCallId = "api-67890";
        String workflowId = "wf-abcde";

        // When
        Map<String, String> headers = hcxHeaderGenerator.generateHeaders(correlationId, apiCallId, workflowId);

        // Then
        assertEquals("test-sender-code", headers.get("x-hcx-sender_code"));
        assertEquals("test-recipient-code", headers.get("x-hcx-recipient_code"));
        assertEquals(correlationId, headers.get("x-hcx-correlation_id"));
        assertEquals(apiCallId, headers.get("x-hcx-api_call_id"));
        assertEquals(workflowId, headers.get("x-hcx-workflow_id"));
        assertNotNull(headers.get("x-hcx-timestamp"));
        assertTrue(Long.parseLong(headers.get("x-hcx-timestamp")) > 0);
    }

    @Test
    void testGenerateHeaders_WithoutWorkflowId_ShouldNotBePresent() {
        // Given
        String correlationId = "corr-12345";
        String apiCallId = "api-67890";

        // When
        Map<String, String> headers = hcxHeaderGenerator.generateHeaders(correlationId, apiCallId, null);

        // Then
        assertFalse(headers.containsKey("x-hcx-workflow_id"));
    }
}
