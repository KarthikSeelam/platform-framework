/**package com.ican.cortex.platform.consul.consul.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDiscoveryClient
@ConfigurationProperties(prefix = "app.consul")
public class DynamicHealthIndicator implements HealthIndicator {

    private boolean dependencyServiceAvailable = true;
    private String serviceName; // Will be bound from app.consul.serviceName
    private int port; // Will be bound from app.consul.port

    public boolean isDependencyServiceAvailable() {
        return dependencyServiceAvailable;
    }

    public void setDependencyServiceAvailable(boolean dependencyServiceAvailable) {
        this.dependencyServiceAvailable = dependencyServiceAvailable;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public Health health() {
        if (dependencyServiceAvailable) {
            return Health.up()
                    .withDetail("status", "Service is healthy")
                    .withDetail("serviceName", serviceName)
                    .withDetail("port", port)
                    .build();
        } else {
            return Health.down()
                    .withDetail("status", "Service is unavailable")
                    .withDetail("serviceName", serviceName)
                    .withDetail("port", port)
                    .build();
        }
    }
}**/
