package com.ican.cortex.platform.incident.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncidentPageResponse {

    private List<IncidentResponse> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
