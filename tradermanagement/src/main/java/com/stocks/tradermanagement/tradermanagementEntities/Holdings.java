package com.stocks.tradermanagement.tradermanagementEntities;

import org.springframework.data.annotation.Id;
import java.time.Year;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Entity
@Table(name = "holdings")
public class Holdings {
    @Id
    @Column(unique = true, nullable = false)
    private String holdingId;

    private String accountId;
    private int stockId;

    @Min(value = 0, message = "BoughtAt must be positive")
    private double boughtAt;

    private double soldAt;

    @Min(value = 0, message = "NumShares must be positive")
    private int numShares;

    private String status;

    private double balance;
    private double entryPrice;
    private double stopLoss;
    private String typeOfPurchase;
    private String typeOfSell;

    @ManyToOne
    @JoinColumn(name="accountId", insertable = false, updatable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "stockId", insertable = false, updatable = false)
    private Stock stock;

    private String symbol;
    private double openPrice;
    private double lastPrice;

    @PrePersist
    private void generateHoldingId() {
        if (stock == null || stock.getSymbol() == null || stock.getSymbol().length() < 2 
            || accountId == null || accountId.length() < 2) {
            throw new IllegalArgumentException("Stock symbol and AccountId must be at least 2 characters long");
        }
        
        // Get first 2 letters from symbol
        String symbolPart = stock.getSymbol().substring(0, 2).toUpperCase();
        
        // Get first 2 letters from accountId
        String accountPart = accountId.substring(0, 2).toUpperCase();
        
        // Get last 2 digits of current year
        String yearPart = String.valueOf(Year.now().getValue()).substring(2);
        
        this.holdingId = symbolPart + accountPart + yearPart;
    }

    public void setStatus(String status) {
        if (status.equals("onhold") || status.equals("bought") || status.equals("canceled") || status.equals("sold")) {
            this.status = status;
        } else {
            throw new IllegalArgumentException("Status must be one of the following: onhold, bought, canceled, sold");
        }
    }
}
