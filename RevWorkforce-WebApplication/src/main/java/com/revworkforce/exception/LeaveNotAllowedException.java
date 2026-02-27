package com.revworkforce.exception;

public class LeaveNotAllowedException extends RuntimeException {

    public LeaveNotAllowedException(String message) {
        super(message);
    }

    public LeaveNotAllowedException(String message, Throwable cause) {
        super(message, cause);
    }
}