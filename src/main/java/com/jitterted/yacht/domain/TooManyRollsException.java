package com.jitterted.yacht.domain;

public class TooManyRollsException extends RuntimeException {
    public TooManyRollsException() {
        super();
    }

    public TooManyRollsException(String message) {
        super(message);
    }
}
