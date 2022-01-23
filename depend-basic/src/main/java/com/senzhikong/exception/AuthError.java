package com.senzhikong.exception;

public class AuthError extends RuntimeException {
    public AuthError() {
        super("");
    }

    public AuthError(String message) {
        super(message);
    }
}
