package com.ican.cortex.platform.incident.app.service;

import com.ican.cortex.platform.incident.api.dto.CreateIncidentRequest;
import com.ican.cortex.platform.incident.domain.entity.Incident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IncidentService {
    Incident createIncident(CreateIncidentRequest request);
    Page<Incident> findIncidents(String status, String priority, String reportedBy, Pageable pageable);
    Incident findIncidentById(UUID id);
}
