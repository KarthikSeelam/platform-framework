package com.ican.cortex.platform.logger.constants;

public enum TransactionType {
    UPLOAD, REFUND, EVENT;

    @Override
    public String toString() {
        return this.name();
    }
}
