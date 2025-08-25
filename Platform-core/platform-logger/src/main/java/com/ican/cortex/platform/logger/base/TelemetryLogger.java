package com.ican.cortex.platform.logger.base;


import org.springframework.stereotype.Component;

/**
     * Logger for telemetry events.
     */
    @Component
    public class TelemetryLogger extends BaseLogger {

    public TelemetryLogger() {
        super(TelemetryLogger.class);
    }


    /**
     * Logs a telemetry event at INFO level.
     */
    public void info(String transactionType, String message) {
        try {

            String correlationId = ensureCorrelationId(); // Ensure correlation ID is available
            prepareLogContext("TELEMETRY", transactionType);
            getLogger().info("[CorrelationId: {}] {}", correlationId, message);
        } finally {
            clearLogContext();
        }
    }

    /**
     * Logs a telemetry error at ERROR level.
     */
    public void error(String transactionType, String message, Throwable throwable) {
        try {
            String correlationId = ensureCorrelationId(); // Ensure correlation ID is available
            prepareLogContext("TELEMETRY", transactionType);
            getLogger().error("[CorrelationId: {}] {}", correlationId, message, throwable);
        } finally {
            clearLogContext();
        }
    }
}

