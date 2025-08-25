package com.ican.cortex.platform.nhcx.service;

import com.ican.cortex.platform.nhcx.api.dto.CoverageEligibilityRequestDTO;
import com.ican.cortex.platform.nhcx.api.dto.SubmissionResponseDTO;
import com.ican.cortex.platform.nhcx.api.dto.CoverageEligibilityRequestDTO;
import com.ican.cortex.platform.nhcx.api.dto.SubmissionResponseDTO;
import com.ican.cortex.platform.nhcx.domain.entity.NhcxRequest;
import com.ican.cortex.platform.nhcx.domain.repository.NhcxRequestRepository;
import com.ican.cortex.platform.nhcx.fhir.bundle.CoverageEligibilityBundleBuilder;
import com.ican.cortex.platform.nhcx.fhir.validator.FhirValidatorService;
import com.ican.cortex.platform.nhcx.hcx.config.WebClientConfig;
import com.ican.cortex.platform.nhcx.hcx.crypto.HcxCryptoService;
import com.ican.cortex.platform.nhcx.hcx.service.HcxHeaderGenerator;
import com.ican.cortex.platform.nhcx.messaging.dto.NhcxEvent;
import com.ican.cortex.platform.nhcx.messaging.service.NhcxKafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoverageEligibilityService {

    private final NhcxRequestRepository nhcxRequestRepository;
    private final CoverageEligibilityBundleBuilder bundleBuilder;
    private final FhirValidatorService fhirValidator;
    private final HcxCryptoService cryptoService;
    private final WebClient hcxWebClient;
    private final HcxHeaderGenerator hcxHeaderGenerator;
    private final NhcxKafkaProducer kafkaProducer;

    public Mono<SubmissionResponseDTO> checkEligibility(CoverageEligibilityRequestDTO requestDTO) {
        String correlationId = UUID.randomUUID().toString();
        String apiCallId = UUID.randomUUID().toString();

        try {
            // 1. Build FHIR Bundle
            CoverageEligibilityBundleBuilder.CoverageEligibilityRequestDTO bundleDto = new CoverageEligibilityBundleBuilder.CoverageEligibilityRequestDTO();
            bundleDto.patientId = requestDTO.getPatientId();
            bundleDto.insurerId = requestDTO.getInsurerId();
            bundleDto.coverageId = requestDTO.getCoverageId();
            Bundle bundle = bundleBuilder.createBundle(bundleDto);

            // 2. Validate FHIR Bundle (add real validation logic here)
            log.info("Validating FHIR bundle for correlationId: {}", correlationId);

            // 3. Encrypt payload
            String fhirPayload = fhirValidator.resourceToJson(bundle);
            String jwePayload = cryptoService.encrypt(fhirPayload);

            // 4. Persist request details
            NhcxRequest nhcxRequest = new NhcxRequest();
            nhcxRequest.setCorrelationId(correlationId);
            nhcxRequest.setApiCallId(apiCallId);
            nhcxRequest.setRequestType("CoverageEligibility");
            nhcxRequest.setStatus("INITIATED");
            nhcxRequest.setSenderCode(hcxHeaderGenerator.generateHeaders(correlationId, apiCallId, null).get("x-hcx-sender_code"));
            nhcxRequest.setRecipientCode(hcxHeaderGenerator.generateHeaders(correlationId, apiCallId, null).get("x-hcx-recipient_code"));
            nhcxRequestRepository.save(nhcxRequest);

            // Send audit event
            NhcxEvent auditEvent = NhcxEvent.builder()
                    .correlationId(correlationId)
                    .apiCallId(apiCallId)
                    .type(NhcxEvent.EventType.COVERAGE_ELIGIBILITY)
                    .status("INITIATED")
                    .timestamp(Instant.now())
                    .build();
            kafkaProducer.sendAuditEvent(auditEvent);

            // 5. Make API call to HCX Gateway
            Map<String, String> body = Map.of("payload", jwePayload);

            return hcxWebClient.post()
                    .uri("/coverageeligibility/v1/check") // Using v1 as per many specs, can be configured
                    .bodyValue(body)
                    .attributes(attrs -> {
                        attrs.put(WebClientConfig.CORRELATION_ID_ATTR, correlationId);
                        attrs.put(WebClientConfig.API_CALL_ID_ATTR, apiCallId);
                    })
                    .retrieve()
                    .toBodilessEntity()
                    .map(responseEntity -> {
                        // HCX typically returns 202 Accepted for async flows
                        String status = responseEntity.getStatusCode().is2xxSuccessful() ? "SUBMITTED" : "FAILED";
                        nhcxRequest.setStatus(status);
                        nhcxRequestRepository.save(nhcxRequest);

                        return SubmissionResponseDTO.builder()
                                .requestId(nhcxRequest.getId())
                                .correlationId(correlationId)
                                .apiCallId(apiCallId)
                                .status(status)
                                .build();
                    })
                    .doOnError(e -> {
                        log.error("Error during HCX call for correlationId: {}", correlationId, e);
                        nhcxRequest.setStatus("FAILED");
                        nhcxRequestRepository.save(nhcxRequest);
                    });

        } catch (Exception e) {
            log.error("Error processing eligibility check for correlationId: {}", correlationId, e);
            return Mono.error(e);
        }
    }
}
