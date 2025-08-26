package com.ican.cortex.platform.incident.api.controller;

import com.ican.cortex.platform.incident.api.dto.CreateIncidentRequest;
import com.ican.cortex.platform.incident.api.dto.IncidentPageResponse;
import com.ican.cortex.platform.incident.api.dto.IncidentResponse;
import com.ican.cortex.platform.incident.app.service.IncidentService;
import com.ican.cortex.platform.incident.domain.entity.Incident;
import com.ican.cortex.platform.incident.support.mapper.IncidentMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/incidents")
@RequiredArgsConstructor
@Tag(name = "Incident Management API", description = "API for creating and managing incidents")
public class IncidentController {

    private final IncidentService incidentService;

    @Operation(summary = "Create a new incident",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Incident details",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreateIncidentRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "title": "Payment service down on checkout",
                                      "description": "Customers cannot complete checkout; 502 from payment gateway.",
                                      "priority": "HIGH",
                                      "reportedBy": "jane.doe@acme.com",
                                      "tags": ["checkout", "payment"],
                                      "attachments": []
                                    }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Incident created successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncidentResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "id": "e7b7a5d0-6c3a-4a42-9b0c-1d2f0b6b1f8a",
                                              "title": "Payment service down on checkout",
                                              "description": "Customers cannot complete checkout; 502 from payment gateway.",
                                              "priority": "HIGH",
                                              "status": "OPEN",
                                              "reportedBy": "jane.doe@acme.com",
                                              "tags": ["checkout","payment"],
                                              "createdAt": "2025-08-26T06:15:27Z",
                                              "updatedAt": "2025-08-26T06:15:27Z"
                                            }
                                            """)))
            })
    @PostMapping
    public ResponseEntity<IncidentResponse> createIncident(@Valid @RequestBody CreateIncidentRequest request) {
        Incident createdIncident = incidentService.createIncident(request);
        return new ResponseEntity<>(IncidentMapper.toResponse(createdIncident), HttpStatus.CREATED);
    }

    @Operation(summary = "Fetch a paginated list of incidents",
            description = "Fetch incidents with optional filters for status, priority, and reporter. Supports pagination and sorting.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved incidents",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncidentPageResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "content": [
                                                {
                                                  "id": "e7b7a5d0-6c3a-4a42-9b0c-1d2f0b6b1f8a",
                                                  "title": "Payment service down on checkout",
                                                  "priority": "HIGH",
                                                  "status": "OPEN",
                                                  "reportedBy": "jane.doe@acme.com",
                                                  "createdAt": "2025-08-26T06:15:27Z"
                                                }
                                              ],
                                              "page": 0,
                                              "size": 20,
                                              "totalElements": 1,
                                              "totalPages": 1
                                            }
                                            """)))
            })
    @GetMapping
    public ResponseEntity<IncidentPageResponse> fetchIncidents(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String reportedBy,
            Pageable pageable) {
        Page<Incident> incidentPage = incidentService.findIncidents(status, priority, reportedBy, pageable);
        return ResponseEntity.ok(IncidentMapper.toPageResponse(incidentPage));
    }

    @Operation(summary = "Fetch a single incident by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved incident",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = IncidentResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Incident not found")
            })
    @GetMapping("/{id}")
    public ResponseEntity<IncidentResponse> fetchIncidentById(@PathVariable UUID id) {
        Incident incident = incidentService.findIncidentById(id);
        return ResponseEntity.ok(IncidentMapper.toResponse(incident));
    }
}
