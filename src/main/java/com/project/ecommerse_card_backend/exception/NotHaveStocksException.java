package com.project.ecommerse_card_backend.exception;


public class NotHaveStocksException extends RuntimeException{
    public NotHaveStocksException(String msg){
        super(msg);
    }
}
