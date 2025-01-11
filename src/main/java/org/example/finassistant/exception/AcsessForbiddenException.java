package org.example.finassistant.exception;

public class AcsessForbiddenException extends RuntimeException{
    public AcsessForbiddenException(String message) {
        super(message);
    }

    public AcsessForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}
