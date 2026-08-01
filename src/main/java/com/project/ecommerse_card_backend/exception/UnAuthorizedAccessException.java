package com.project.ecommerse_card_backend.exception;

public class UnAuthorizedAccessException extends RuntimeException{
    public UnAuthorizedAccessException(String msg){
        super(msg);
    }
}
