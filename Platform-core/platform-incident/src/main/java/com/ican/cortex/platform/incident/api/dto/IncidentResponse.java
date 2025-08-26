package com.ican.cortex.platform.incident.api.dto;

import lombok.Data;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
public class IncidentResponse {

    private UUID id;
    private String title;
    private String description;
    private String priority;
    private String status;
    private String reportedBy;
    private List<String> tags;
    private Instant createdAt;
    private Instant updatedAt;
}
