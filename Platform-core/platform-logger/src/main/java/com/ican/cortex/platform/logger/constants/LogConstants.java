package com.ican.cortex.platform.logger.constants;

/**
 * A utility class that defines constants for log-related metadata keys.
 */
public class LogConstants {

    /**
     * The key for the correlation ID in the logging context.
     */
    public static final String CORRELATION_ID = "correlationId";

    /**
     * The key for the operation type in the logging context.
     * Represents the type of the operation being logged (e.g., FILE, TELEMETRY, PAYMENT).
     */
    public static final String OPERATION_TYPE = "operationType"; // will by code

    /**
     * The key for the transaction type in the logging context.
     * Represents the specific transaction being logged (e.g., UPLOAD, REFUND, EVENT).
     */
    public static final String TRANSACTION_TYPE = "transactionType"; //

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private LogConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated.");
    }
}

