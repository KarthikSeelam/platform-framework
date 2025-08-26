package com.ican.cortex.platform.incident.app.service;

import com.ican.cortex.platform.incident.api.dto.CreateIncidentRequest;
import com.ican.cortex.platform.incident.api.exception.ResourceNotFoundException;
import com.ican.cortex.platform.incident.app.spec.IncidentSpecifications;
import com.ican.cortex.platform.incident.domain.entity.Incident;
import com.ican.cortex.platform.incident.domain.repo.IncidentRepository;
import com.ican.cortex.platform.incident.support.mapper.IncidentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository incidentRepository;
    private static final Set<String> VALID_PRIORITIES = Set.of("HIGH", "MEDIUM", "LOW");

    @Override
    @Transactional
    public Incident createIncident(CreateIncidentRequest request) {
        log.info("Creating a new incident with title: {}", request.getTitle());
        validatePriority(request.getPriority());

        Incident incident = IncidentMapper.toEntity(request);
        Incident savedIncident = incidentRepository.save(incident);

        log.info("Successfully created incident with ID: {}", savedIncident.getId());
        return savedIncident;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Incident> findIncidents(String status, String priority, String reportedBy, Pageable pageable) {
        log.info("Fetching incidents with filters - status: {}, priority: {}, reportedBy: {}", status, priority, reportedBy);
        Page<Incident> incidentPage = incidentRepository.findAll(
                IncidentSpecifications.withFilters(status, priority, reportedBy),
                pageable
        );
        log.info("Found {} incidents on page {} of {}", incidentPage.getNumberOfElements(), incidentPage.getNumber(), incidentPage.getTotalPages());
        return incidentPage;
    }

    @Override
    @Transactional(readOnly = true)
    public Incident findIncidentById(UUID id) {
        log.info("Fetching incident with ID: {}", id);
        return incidentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Incident not found with ID: {}", id);
                    return new ResourceNotFoundException("Incident not found with id: " + id);
                });
    }

    private void validatePriority(String priority) {
        if (priority == null || !VALID_PRIORITIES.contains(priority.toUpperCase())) {
            throw new IllegalArgumentException("Invalid priority value. Allowed values are HIGH, MEDIUM, LOW.");
        }
    }
}
