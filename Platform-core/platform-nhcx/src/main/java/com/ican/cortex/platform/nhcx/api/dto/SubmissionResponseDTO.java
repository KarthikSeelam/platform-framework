package com.ican.cortex.platform.nhcx.api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SubmissionResponseDTO {
    private UUID requestId;
    private String correlationId;
    private String apiCallId;
    private String status;
}
