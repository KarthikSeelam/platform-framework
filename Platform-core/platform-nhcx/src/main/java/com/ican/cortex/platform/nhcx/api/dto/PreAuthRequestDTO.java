package com.ican.cortex.platform.nhcx.api.dto;

import lombok.Data;

@Data
public class PreAuthRequestDTO {
    private String patientId;
    private String providerId;
    private String insurerId;
    private String coverageId;
    // In a real scenario, this DTO would be much richer,
    // containing lists of diagnoses, procedures, items, costs etc.
}
