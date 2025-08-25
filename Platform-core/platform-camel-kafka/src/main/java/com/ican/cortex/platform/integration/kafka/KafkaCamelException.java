package com.ican.cortex.platform.integration.kafka;

public class KafkaCamelException extends RuntimeException {

    private final ErrorCode errorCode;

    public enum ErrorCode {
        MESSAGE_SEND_ERROR,
        CAMEL_CONTEXT_START_ERROR,
        CAMEL_CONTEXT_STOP_ERROR
    }

    public KafkaCamelException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public KafkaCamelException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
