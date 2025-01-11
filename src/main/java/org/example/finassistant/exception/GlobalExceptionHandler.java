package org.example.finassistant.exception;

import org.example.finassistant.exception.DataNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<String> handleDataNotFound(DataNotFoundException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(AcsessForbiddenException.class)
    public ResponseEntity<String> handleAcsessDenied(AcsessForbiddenException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.FORBIDDEN);
    }
}
