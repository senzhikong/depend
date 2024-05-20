package com.senzhikong.basic.exception;

/**
 * @author shu
 */
public class DataException extends RuntimeException {
    public DataException() {
        super("");
    }

    public DataException(String message) {
        super(message);
    }
}
