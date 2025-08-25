/**package com.ican.cortex.platform.consul;

import com.ican.cortex.platform.consul.consul.config.DynamicHealthIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

    @Autowired
    private DynamicHealthIndicator healthIndicator;

    @PostMapping("/dependency/up")
    public String setDependencyUp() {
        healthIndicator.setDependencyServiceAvailable(true);
        return "Dependency service is marked as UP.";
    }

    @PostMapping("/dependency/down")
    public String setDependencyDown() {
        healthIndicator.setDependencyServiceAvailable(false);
        return "Dependency service is marked as DOWN.";
    }

    @GetMapping
    public String healthCheck() {
        return "Service is up and running!";
    }
}**/

