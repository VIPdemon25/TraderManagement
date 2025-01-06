package com.stocks.tradermanagement.exceptions;

public class AccountIdDontOwnTheStock extends RuntimeException {
    public AccountIdDontOwnTheStock(String message) {
        super(message);
    }
}
