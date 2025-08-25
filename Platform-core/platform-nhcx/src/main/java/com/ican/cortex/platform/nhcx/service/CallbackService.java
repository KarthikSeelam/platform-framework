package com.ican.cortex.platform.nhcx.service;

import com.ican.cortex.platform.nhcx.domain.entity.NhcxRequest;
import com.ican.cortex.platform.nhcx.domain.repository.NhcxRequestRepository;
import com.ican.cortex.platform.nhcx.fhir.validator.FhirValidatorService;
import com.ican.cortex.platform.nhcx.hcx.crypto.HcxCryptoService;
import com.ican.cortex.platform.nhcx.messaging.dto.NhcxEvent;
import com.ican.cortex.platform.nhcx.messaging.service.NhcxKafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.ClaimResponse;
import org.hl7.fhir.r4.model.CoverageEligibilityResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CallbackService {

    private final HcxCryptoService cryptoService;
    private final FhirValidatorService fhirValidator;
    private final NhcxRequestRepository requestRepository;
    private final NhcxKafkaProducer kafkaProducer;

    public Mono<Void> handleCallback(Map<String, String> body) {
        return Mono.fromRunnable(() -> {
            String jwePayload = body.get("payload");
            if (jwePayload == null) {
                log.error("Callback received without a JWE payload.");
                return;
            }

            try {
                // 1. Decrypt payload
                String fhirJson = cryptoService.decrypt(jwePayload);
                log.debug("Decrypted FHIR payload: {}", fhirJson);

                // 2. Parse and Validate FHIR Bundle
                Bundle bundle = fhirValidator.jsonToResource(fhirJson, Bundle.class);
                // TODO: Add specific profile validation

                // 3. Extract correlation ID from the response bundle
                String correlationId = findCorrelationId(bundle);

                if (correlationId == null) {
                    log.error("Could not extract correlation ID from callback bundle.");
                    return;
                }

                // 4. Update status in database
                Optional<NhcxRequest> optionalRequest = requestRepository.findByCorrelationId(correlationId);
                if (optionalRequest.isPresent()) {
                    NhcxRequest request = optionalRequest.get();
                    // TODO: Determine status from bundle outcome
                    request.setStatus("COMPLETED");
                    requestRepository.save(request);
                    log.info("Updated status for correlation ID {}: {}", correlationId, request.getStatus());

                    // 5. Persist the response payload (TBD)

                    // 6. Emit Kafka event
                    NhcxEvent event = NhcxEvent.builder()
                            .correlationId(correlationId)
                            .apiCallId(request.getApiCallId())
                            .type(request.getRequestType().equals("CoverageEligibility") ? NhcxEvent.EventType.COVERAGE_ELIGIBILITY : NhcxEvent.EventType.PRE_AUTHORIZATION)
                            .status("CALLBACK_PROCESSED")
                            .timestamp(Instant.now())
                            .outcomeSummary("Callback received and processed successfully.") // TODO: get from bundle
                            .build();
                    kafkaProducer.sendEvent(event);

                } else {
                    log.warn("Received callback for an unknown correlation ID: {}", correlationId);
                }

            } catch (Exception e) {
                log.error("Error processing callback", e);
            }
        });
    }

    private String findCorrelationId(Bundle bundle) {
        return bundle.getEntry().stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(r -> r instanceof CoverageEligibilityResponse || r instanceof ClaimResponse)
                .findFirst()
                .map(r -> {
                    if (r instanceof CoverageEligibilityResponse) {
                        return ((CoverageEligibilityResponse) r).getRequest().getIdentifier().getValue();
                    } else if (r instanceof ClaimResponse) {
                        return ((ClaimResponse) r).getRequest().getIdentifier().getValue();
                    }
                    return null;
                })
                .orElse(null);
    }
}
