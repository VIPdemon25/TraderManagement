package com.stocks.tradermanagement.tradermanagementDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionStatusDTO {
    private String status;
    private int sharesTraded;
    private double price;
    private String transactionType;  // "BUY" or "SELL"
    private String message;
} 