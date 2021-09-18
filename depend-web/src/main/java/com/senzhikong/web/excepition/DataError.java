package com.senzhikong.web.excepition;

public class DataError extends RuntimeException {
    public DataError() {
        super("");
    }

    public DataError(String message) {
        super(message);
    }
}
