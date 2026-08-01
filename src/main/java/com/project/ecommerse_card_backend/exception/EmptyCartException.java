package com.project.ecommerse_card_backend.exception;


public class EmptyCartException extends RuntimeException{
    public EmptyCartException(String msg){
        super(msg);
    }
}
