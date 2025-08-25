package com.ican.cortex.platform.logger.base;

import org.springframework.stereotype.Component;

@Component
public class APILogger extends BaseLogger {

    public static String upload = "Upload";

    public APILogger() {
        super(APILogger.class);
    }

    /**
     * Logs an API operation at INFO level.
     *
     * @param transactionType The transaction type (e.g., REQUEST, RESPONSE).
     * @param message         The log message.
     */
    public void info(String transactionType, String message) {
        try {
            String correlationId = ensureCorrelationId(); // Ensure correlation ID exists
            prepareLogContext("API", transactionType);
            getLogger().info("[CorrelationId: {}] {}", correlationId, message);
        } finally {
            clearLogContext();
        }
    }

    /**
     * Logs an API operation at ERROR level.
     *
     * @param transactionType The transaction type (e.g., REQUEST, RESPONSE).
     * @param message         The error message.
     * @param throwable       The exception to log.
     */
    public void error(String transactionType, String message, Throwable throwable) {
        try {
            String correlationId = ensureCorrelationId(); // Ensure correlation ID exists
            prepareLogContext("API", transactionType);
            getLogger().error("[CorrelationId: {}] {}", correlationId, message, throwable);
        } finally {
            clearLogContext();
        }
    }

    /**
     * Logs an API operation at DEBUG level.
     *
     * @param transactionType The transaction type (e.g., REQUEST, RESPONSE).
     * @param message         The debug log message.
     */
    public void debug(String transactionType, String message) {
        try {
            String correlationId = ensureCorrelationId(); // Ensure correlation ID exists
            prepareLogContext("API", transactionType);
            getLogger().debug("[CorrelationId: {}] {}", correlationId, message);
        } finally {
            clearLogContext();
        }
    }
}