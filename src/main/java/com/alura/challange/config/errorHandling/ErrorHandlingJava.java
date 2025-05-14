package com.alura.challange.config.errorHandling;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandlingJava {

    @ExceptionHandler(ErrorHandlingValidation.class)
    public ResponseEntity serviceErrorHandler(ErrorHandlingValidation ex){

        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity invalidJsonHandler(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body("Invalid fields for the form!");
    }
}
