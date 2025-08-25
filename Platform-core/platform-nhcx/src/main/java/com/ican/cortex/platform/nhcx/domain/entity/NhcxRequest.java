package com.ican.cortex.platform.nhcx.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "nhcx_request", indexes = {
        @Index(name = "idx_api_call_id", columnList = "apiCallId"),
        @Index(name = "idx_correlation_id", columnList = "correlationId")
})
@Getter
@Setter
public class NhcxRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "request_type", nullable = false)
    private String requestType;

    @Column(name = "api_call_id", unique = true, nullable = false)
    private String apiCallId;

    @Column(name = "correlation_id", nullable = false)
    private String correlationId;

    @Column(name = "workflow_id")
    private String workflowId;

    @Column(name = "sender_code", nullable = false)
    private String senderCode;

    @Column(name = "recipient_code", nullable = false)
    private String recipientCode;

    @Column(nullable = false)
    private String status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
