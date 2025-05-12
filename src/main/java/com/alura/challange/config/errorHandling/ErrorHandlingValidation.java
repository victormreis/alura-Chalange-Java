package com.alura.challange.config.errorHandling;

public class ErrorHandlingValidation extends RuntimeException {

    public ErrorHandlingValidation(String message){
        super(message);
    }

}
