package com.ican.cortex.platform.incident.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;

@Data
public class CreateIncidentRequest {

    @NotBlank(message = "Title is mandatory")
    private String title;

    private String description;

    @NotBlank(message = "Priority is mandatory")
    private String priority;

    private String reportedBy;

    private List<String> tags;

    private List<String> attachments;
}
