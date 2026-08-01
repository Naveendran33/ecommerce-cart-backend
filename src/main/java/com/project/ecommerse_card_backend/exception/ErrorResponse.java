package com.project.ecommerse_card_backend.exception;


public record ErrorResponse(Integer status, String error, String message, long timeStamp) {
}