package com.ican.cortex.platform.nhcx.fhir.bundle;

import org.hl7.fhir.r4.model.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class ClaimBundleBuilder {

    public static class PreAuthRequestDTO {
        public String patientId;
        public String providerId;
        public String insurerId;
        public String coverageId;
        // Add other fields from the internal API DTO as needed
        // e.g., diagnosis, procedures, items, costs
    }

    public Bundle createBundle(PreAuthRequestDTO dto) {
        Bundle bundle = new Bundle();
        bundle.setId(UUID.randomUUID().toString());
        bundle.setType(Bundle.BundleType.COLLECTION);
        bundle.setTimestamp(new Date());

        // Create the Claim resource for Pre-Authorization
        Claim claim = new Claim();
        claim.setId(UUID.randomUUID().toString());
        claim.setStatus(Claim.ClaimStatus.ACTIVE);
        // This is crucial for pre-authorization
        claim.setUse(Claim.Use.PREAUTHORIZATION);
        claim.setCreated(new Date());

        // Set patient reference
        claim.setPatient(new Reference("Patient/" + dto.patientId));

        // Set provider reference
        claim.setProvider(new Reference("Organization/" + dto.providerId));

        // Set insurer reference
        claim.setInsurer(new Reference("Organization/" + dto.insurerId));

        // Set priority
        claim.setPriority(new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/processpriority", "normal", "Normal")));

        // Add insurance information
        claim.getInsurance().add(new Claim.InsuranceComponent()
                .setSequence(1)
                .setFocal(true)
                .setCoverage(new Reference("Coverage/" + dto.coverageId)));

        // Other details like diagnosis, procedures, items would be added here
        // For example:
        // claim.addDiagnosis(new Claim.DiagnosisComponent().setDiagnosisReference(new Reference("Condition/c279")));

        // Add the claim to the bundle as an entry
        bundle.addEntry()
                .setFullUrl("urn:uuid:" + claim.getId())
                .setResource(claim);

        return bundle;
    }
}
