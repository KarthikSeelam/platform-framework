package com.ican.cortex.platform.api.dto.payloads;


public class Result<T> extends ResultBase {
    private T data;

    public Result() {
        super();
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}