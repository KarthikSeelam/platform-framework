package com.ican.cortex.platform.nhcx.messaging.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class NhcxEvent {
    private String correlationId;
    private String apiCallId;
    private EventType type;
    private String status;
    private Instant timestamp;
    private String outcomeSummary; // e.g., "Eligibility confirmed" or "Pre-auth rejected"

    public enum EventType {
        COVERAGE_ELIGIBILITY,
        PRE_AUTHORIZATION
    }
}
