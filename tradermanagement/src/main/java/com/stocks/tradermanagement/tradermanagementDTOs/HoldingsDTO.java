package com.stocks.tradermanagement.tradermanagementDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoldingsDTO {
    private String holdingId; // Match with Holdings entity
    private String accountId;
    private int stockId;
    private double boughtAt;
    private double soldAt;
    private int numShares;
    private String status;
    private double balance;
    private double entryPrice;
    private double stopLoss;
    private String typeOfPurchase;
    private String typeOfSell;
    private String symbol;
    private double openPrice;
    private double lastPrice;
    
}