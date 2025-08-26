package com.ican.cortex.platform.incident.app.spec;

import com.ican.cortex.platform.incident.domain.entity.Incident;
import com.ican.cortex.platform.incident.domain.model.IncidentStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public final class IncidentSpecifications {

    public static Specification<Incident> withFilters(String status, String priority, String reportedBy) {
        return Specification.where(hasStatus(status))
                .and(hasPriority(priority))
                .and(hasReportedBy(reportedBy));
    }

    private static Specification<Incident> hasStatus(String status) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(status)) {
                return criteriaBuilder.conjunction();
            }
            try {
                IncidentStatus incidentStatus = IncidentStatus.valueOf(status.toUpperCase());
                return criteriaBuilder.equal(root.get("status"), incidentStatus);
            } catch (IllegalArgumentException e) {
                return criteriaBuilder.disjunction();
            }
        };
    }

    private static Specification<Incident> hasPriority(String priority) {
        return (root, query, criteriaBuilder) ->
                StringUtils.hasText(priority) ? criteriaBuilder.equal(root.get("priority"), priority) : criteriaBuilder.conjunction();
    }

    private static Specification<Incident> hasReportedBy(String reportedBy) {
        return (root, query, criteriaBuilder) ->
                StringUtils.hasText(reportedBy) ? criteriaBuilder.equal(root.get("reportedBy"), reportedBy) : criteriaBuilder.conjunction();
    }
}
