package com.project.ecommerse_card_backend.exception;

public class CannotCancelOrderException extends RuntimeException{
    public CannotCancelOrderException(String msg){
        super(msg);
    }
}
