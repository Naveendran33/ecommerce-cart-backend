package com.project.ecommerse_card_backend.exception;

public class CouponNotEligibleException extends RuntimeException{
    public CouponNotEligibleException(String msg){
        super(msg);
    }
}
