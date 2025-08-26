package com.ican.cortex.platform.incident.support.mapper;

import com.ican.cortex.platform.incident.api.dto.CreateIncidentRequest;
import com.ican.cortex.platform.incident.api.dto.IncidentPageResponse;
import com.ican.cortex.platform.incident.api.dto.IncidentResponse;
import com.ican.cortex.platform.incident.domain.entity.Incident;
import com.ican.cortex.platform.incident.domain.model.IncidentStatus;
import org.springframework.data.domain.Page;

import java.util.stream.Collectors;

public class IncidentMapper {

    public static Incident toEntity(CreateIncidentRequest request) {
        if (request == null) {
            return null;
        }
        Incident incident = new Incident();
        incident.setTitle(request.getTitle());
        incident.setDescription(request.getDescription());
        incident.setPriority(request.getPriority());
        incident.setReportedBy(request.getReportedBy());
        // The service will set the initial status.
        incident.setStatus(IncidentStatus.OPEN);
        // Tags and attachments are not yet modeled in the entity.
        // This can be added later if needed.
        return incident;
    }

    public static IncidentResponse toResponse(Incident entity) {
        if (entity == null) {
            return null;
        }
        IncidentResponse response = new IncidentResponse();
        response.setId(entity.getId());
        response.setTitle(entity.getTitle());
        response.setDescription(entity.getDescription());
        response.setPriority(entity.getPriority());
        response.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        response.setReportedBy(entity.getReportedBy());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        // Tags and attachments are not yet modeled in the entity.
        return response;
    }

    public static IncidentPageResponse toPageResponse(Page<Incident> page) {
        if (page == null) {
            return null;
        }
        return new IncidentPageResponse(
                page.getContent().stream().map(IncidentMapper::toResponse).collect(Collectors.toList()),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
