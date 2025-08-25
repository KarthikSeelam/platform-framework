package com.ican.cortex.platform.nhcx.fhir.validator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.r4.model.Resource;
import org.springframework.stereotype.Service;

@Service
public class FhirValidatorService {

    private final FhirValidator validator;
    private final FhirContext ctx;

    public FhirValidatorService() {
        this.ctx = FhirContext.forR4();
        // The validator can be configured with profiles and other rules.
        // For now, it will perform basic validation against the base FHIR spec.
        // In a real implementation, we would load the HCX profiles here.
        // e.g., validator.registerValidatorModule(new SchemaBaseValidator(ctx));
        // validator.registerValidatorModule(new InstanceValidator(validationSupportChain));
        this.validator = ctx.newValidator();
    }

    /**
     * Validates a FHIR resource.
     * @param resource The resource to validate.
     * @return The result of the validation.
     */
    public ValidationResult validate(Resource resource) {
        return validator.validateWithResult(resource);
    }

    /**
     * Serializes a FHIR resource to a JSON string.
     * @param resource The resource to serialize.
     * @return The JSON string representation of the resource.
     */
    public String resourceToJson(Resource resource) {
        return newJsonParser().encodeResourceToString(resource);
    }

    /**
     * Parses a FHIR resource from a JSON string.
     * @param json The JSON string to parse.
     * @param resourceType The class of the resource to parse.
     * @return The parsed resource.
     */
    public <T extends Resource> T jsonToResource(String json, Class<T> resourceType) {
        return newJsonParser().parseResource(resourceType, json);
    }

    private IParser newJsonParser() {
        IParser parser = ctx.newJsonParser();
        parser.setPrettyPrint(true);
        return parser;
    }

    public FhirContext getFhirContext() {
        return ctx;
    }
}
