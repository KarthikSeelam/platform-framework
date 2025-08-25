package com.ican.cortex.platform.logger.base;


import com.ican.cortex.platform.logger.constants.LogConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * Base class for all custom loggers. Provides common functionality for logging with metadata.
 */
public abstract class BaseLogger {

    private final Logger logger;

    protected BaseLogger(Class<?> clazz) {
        this.logger = LogManager.getLogger(clazz); // Bind logger to the extending class
    }

    /**
     * Ensures that the correlation ID is available.
     *
     * @return The existing or newly generated correlation ID.
     */
    protected String ensureCorrelationId() {
        String correlationId = MDC.get(LogConstants.CORRELATION_ID);
        if (correlationId == null) {
            correlationId = generateCorrelationId(); // Generate a new correlation ID if not present
            MDC.put(LogConstants.CORRELATION_ID, correlationId);
        }
        return correlationId;
    }

    /**
     * Prepares the log context with metadata.
     *
     * @param operationType   The operation type (e.g., FILE, TELEMETRY).
     * @param transactionType The transaction type (e.g., UPLOAD, EVENT).
     */
    protected void prepareLogContext(String operationType, String transactionType) {
        MDC.put(LogConstants.OPERATION_TYPE, operationType);
        MDC.put(LogConstants.TRANSACTION_TYPE, transactionType);
    }

    /**
     * Clears the log context except for the correlation ID.
     */
    protected void clearLogContext() {
        MDC.remove(LogConstants.OPERATION_TYPE);
        MDC.remove(LogConstants.TRANSACTION_TYPE);
    }

    /**
     * Returns the logger instance for the subclass.
     *
     * @return The logger instance.
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * Generates a unique correlation ID.
     *
     * @return A new correlation ID.
     */
    private String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }
}


