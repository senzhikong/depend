package com.senzhikong.basic.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author shu
 */
@Getter
@Setter
public class DataException extends RuntimeException {
    private Integer code;

    public DataException() {
        super("");
    }

    public DataException(String message) {
        super(message);
    }

    public DataException(String message, Integer code) {
        super(message);
        this.code = code;
    }
}
