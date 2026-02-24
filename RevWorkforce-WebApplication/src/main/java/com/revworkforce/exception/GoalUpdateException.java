package com.revworkforce.exception;

public class GoalUpdateException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public GoalUpdateException(String message) {
        super(message);
    }
}