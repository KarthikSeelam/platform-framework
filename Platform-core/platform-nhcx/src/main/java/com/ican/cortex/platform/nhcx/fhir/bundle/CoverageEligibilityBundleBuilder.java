package com.ican.cortex.platform.nhcx.fhir.bundle;

import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.UUID;
import java.util.List;

@Component
public class CoverageEligibilityBundleBuilder {

    public static class CoverageEligibilityRequestDTO {
        public String patientId;
        public String insurerId;
        public String coverageId;
        // Add other fields from the internal API DTO as needed
    }

    public Bundle createBundle(CoverageEligibilityRequestDTO dto) {
        Bundle bundle = new Bundle();
        bundle.setId(UUID.randomUUID().toString());
        bundle.setType(Bundle.BundleType.COLLECTION);
        bundle.setTimestamp(new Date());

        // Create the CoverageEligibilityRequest resource
        CoverageEligibilityRequest request = new CoverageEligibilityRequest();
        request.setId(UUID.randomUUID().toString());
        request.setStatus(CoverageEligibilityRequest.EligibilityRequestStatus.ACTIVE);
        request.setPurpose(List.of(
                new CodeType(CoverageEligibilityRequest.EligibilityRequestPurpose.BENEFITS.toCode()),
                new CodeType(CoverageEligibilityRequest.EligibilityRequestPurpose.VALIDATION.toCode())
        ));
        request.setCreated(new Date());

        // Set patient reference
        request.setPatient(new Reference("Patient/" + dto.patientId));

        // Set insurer reference
        request.setInsurer(new Reference("Organization/" + dto.insurerId));

        // Add coverage reference
        request.getInsurance().add(new CoverageEligibilityRequest.InsuranceComponent()
                .setFocal(true)
                .setCoverage(new Reference("Coverage/" + dto.coverageId)));

        // Add the request to the bundle as an entry
        bundle.addEntry()
                .setFullUrl("urn:uuid:" + request.getId())
                .setResource(request);

        return bundle;
    }
}
