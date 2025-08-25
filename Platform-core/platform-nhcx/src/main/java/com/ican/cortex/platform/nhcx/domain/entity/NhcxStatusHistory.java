package com.ican.cortex.platform.nhcx.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "nhcx_status_history", indexes = {
        @Index(name = "idx_correlation_id_history", columnList = "correlationId")
})
@Getter
@Setter
public class NhcxStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "correlation_id", nullable = false)
    private String correlationId;

    @Column(nullable = false)
    private String status;

    @Lob
    @Column(name = "details_json", columnDefinition = "TEXT")
    private String detailsJson;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime timestamp;
}
