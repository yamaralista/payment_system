package com.example.payment_system_v3.exception;

public class InvalidPinException extends Exception{
    public InvalidPinException(String message) {
        super(message);
    }
}
