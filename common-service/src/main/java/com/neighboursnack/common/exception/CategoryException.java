package com.neighboursnack.common.exception;

public class CategoryException extends RuntimeException {

    public CategoryException() {
        super("Category error occurred");
    }

    public CategoryException(String message) {
        super(message);
    }

    public CategoryException(String message, Throwable cause) {
        super(message, cause);
    }

}