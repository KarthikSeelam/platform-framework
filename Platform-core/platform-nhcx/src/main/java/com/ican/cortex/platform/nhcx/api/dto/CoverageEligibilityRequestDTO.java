package com.ican.cortex.platform.nhcx.api.dto;

import lombok.Data;

@Data
public class CoverageEligibilityRequestDTO {
    private String patientId;
    private String insurerId;
    private String coverageId;
}
