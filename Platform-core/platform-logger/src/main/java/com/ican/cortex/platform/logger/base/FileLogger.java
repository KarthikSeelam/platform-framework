package com.ican.cortex.platform.logger.base;

import com.ican.cortex.platform.logger.constants.TransactionType;
import com.ican.cortex.platform.logger.caller.LoggableCaller;

import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.stereotype.Component;

@Component
public class FileLogger extends BaseLogger {

    public static String upload = "Upload";

    public FileLogger() {
        super(FileLogger.class);
    }

    public void info(LoggableCaller caller, TransactionType transactionType, String message) {
        try {
            String correlationId = ensureCorrelationId();
            String callerClass = (caller != null)
                    ? AopProxyUtils.ultimateTargetClass(caller).getName()
                    : "UnknownCaller";
            prepareLogContext("File", transactionType.toString());
            getLogger().info("[CorrelationId: {}] [Caller: {}] [TransactionType: {}] {}",
                    correlationId, callerClass, transactionType.toString(), message);
        } finally {
            clearLogContext();
        }
    }

    public void info(LoggableCaller caller, String transactionType, String message) {
        try {
            String correlationId = ensureCorrelationId();
            String callerClass = (caller != null)
                    ? AopProxyUtils.ultimateTargetClass(caller).getName() // Get the original class name with package
                    : "UnknownCaller";
            prepareLogContext("File", transactionType);
            getLogger().info("[CorrelationId: {}] [Caller: {}] {}", correlationId, callerClass, message);
        } finally {
            clearLogContext();
        }
    }

    /**
     * Logs a file operation at INFO level.
     *
     * @param transactionType The transaction type (e.g., UPLOAD, DOWNLOAD).
     * @param message         The log message.
     */
    public void info(String transactionType, String message) {
        try {
            String callingClass =  getCallingClass();
            String correlationId = ensureCorrelationId(); // Ensure correlation ID exists
            prepareLogContext("File", transactionType);
            getLogger().info("[CorrelationId: {}] {} {}", correlationId, callingClass ,message);
        } finally {
            clearLogContext();
        }
    }

    /**
     * Logs a file operation at ERROR level.
     *

     * @param transactionType The transaction type (e.g., UPLOAD, DOWNLOAD).
     * @param message         The error message.
     * @param throwable       The exception to log.
     */
    public void error(String transactionType, String message, Throwable throwable) {
        try {
            String callingClass =  getCallingClass();
            String correlationId = ensureCorrelationId(); // Ensure correlation ID exists
            prepareLogContext("File", transactionType);
            getLogger().error("[CorrelationId: {}] {} {}", correlationId,callingClass,  message, throwable);
        } finally {
            clearLogContext();
        }
    }

    /**
     * Logs a custom message for file operations at DEBUG level.
     *

     * @param transactionType The transaction type (e.g., UPLOAD, DOWNLOAD).
     * @param message         The custom log message.
     */
    public void debug(String transactionType, String message) {
        try {
            String correlationId = ensureCorrelationId(); // Ensure correlation ID exists
            prepareLogContext("File", transactionType);
            getLogger().debug("[CorrelationId: {}] {}", correlationId, message);
        } finally {
            clearLogContext();
        }
    }

    private String getCallingClass() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            // Skip internal logger calls and find the first external caller
            if (!element.getClassName().equals(this.getClass().getName())
                    && !element.getClassName().contains("java.lang.Thread")) {
                return element.getClassName();
            }
        }
        return "UnknownCaller"; // Fallback if no caller is found
    }
}