package com.ican.cortex.platform.nhcx.api.controller;

import com.ican.cortex.platform.nhcx.api.dto.CoverageEligibilityRequestDTO;
import com.ican.cortex.platform.nhcx.api.dto.PreAuthRequestDTO;
import com.ican.cortex.platform.nhcx.api.dto.SubmissionResponseDTO;
import com.ican.cortex.platform.nhcx.service.CoverageEligibilityService;
import com.ican.cortex.platform.nhcx.service.PreAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/nhcx")
@RequiredArgsConstructor
public class NhcxController {

    private final CoverageEligibilityService coverageEligibilityService;
    private final PreAuthService preAuthService;

    @PostMapping("/coverage-eligibility/check")
    public Mono<SubmissionResponseDTO> checkEligibility(@RequestBody CoverageEligibilityRequestDTO requestDTO) {
        return coverageEligibilityService.checkEligibility(requestDTO);
    }

    @GetMapping("/coverage-eligibility/status/{correlationId}")
    public Mono<String> getEligibilityStatus(@PathVariable String correlationId) {
        // In a real implementation, this would call a service that fetches the status
        // from the database or calls the HCX status check endpoint.
        return Mono.just("Status for eligibility request " + correlationId);
    }

    @PostMapping("/preauth/submit")
    public Mono<SubmissionResponseDTO> submitPreAuth(@RequestBody PreAuthRequestDTO requestDTO) {
        return preAuthService.submitPreAuth(requestDTO);
    }

    @GetMapping("/preauth/status/{correlationId}")
    public Mono<String> getPreAuthStatus(@PathVariable String correlationId) {
        // Similar to eligibility status
        return Mono.just("Status for pre-auth request " + correlationId);
    }
}
