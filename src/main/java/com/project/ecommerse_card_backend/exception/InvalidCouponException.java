package com.project.ecommerse_card_backend.exception;

public class InvalidCouponException extends RuntimeException{
    public InvalidCouponException(String msg){
        super(msg);
    }
}
