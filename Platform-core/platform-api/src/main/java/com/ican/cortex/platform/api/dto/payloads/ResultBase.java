package com.ican.cortex.platform.api.dto.payloads;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ResultBase {
    private final List<String> errors;

    public ResultBase() {
        this.errors = new ArrayList<String>();
    }

    public void addError(String error) {
        this.errors.add(error);
    }

    public void addError(Exception ex) {
        this.errors.add(ex.getMessage());
    }

    public boolean isSuccess() {
        return this.errors.size() == 0;
    }

    public void copyFrom(ResultBase result) {
        if (result == null || result.errors == null)
            return;
        for (var error : result.errors) {
            this.addError(error);
        }
    }

    public <TData> TData copyFrom(Result<TData> result) {
        if (result == null)
            return null;

        copyFrom((ResultBase) result);

        return result.getData();
    }
}
