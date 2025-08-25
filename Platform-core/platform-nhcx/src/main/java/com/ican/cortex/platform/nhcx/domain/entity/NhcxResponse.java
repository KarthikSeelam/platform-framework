package com.ican.cortex.platform.nhcx.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "nhcx_response")
@Getter
@Setter
public class NhcxResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    private NhcxRequest request;

    @Column(name = "transport_status")
    private String transportStatus;

    @Column(name = "http_status")
    private Integer httpStatus;

    @Column(name = "error_code")
    private String errorCode;

    @Lob
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
