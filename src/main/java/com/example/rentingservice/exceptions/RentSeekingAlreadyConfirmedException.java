package com.example.rentingservice.exceptions;

public class RentSeekingAlreadyConfirmedException extends BusinessException {
    public RentSeekingAlreadyConfirmedException(String msg) {
        super(4001, msg);
    }
}
