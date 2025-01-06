package com.stocks.tradermanagement.exceptions;

public class StockAlreadySoldException extends RuntimeException {
    public StockAlreadySoldException(String message) {
        super(message);
    }
}
