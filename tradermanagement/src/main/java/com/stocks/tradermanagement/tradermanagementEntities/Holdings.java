package com.stocks.tradermanagement.tradermanagementEntities;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    private String stockId;

    @Min(value = 0, message = "BoughtAt must be positive")
    private double boughtAt;

    private double soldAt;

    @Min(value = 0, message = "NumShares must be positive")
    private int numShares;

    private String status;

    // New fields added
    private double balance;
    private double entryPrice;
    private double stopLoss;
    private String typeOfPurchase;
    private String typeOfSell;

    @ManyToOne
    @JoinColumn(name="accountId")
    private Account account;

    public void setStatus(String status) {
        if (status.equals("onhold") || status.equals("bought") || status.equals("canceled") || status.equals("sold")) {
            this.status = status;
        } else {
            throw new IllegalArgumentException("Status must be one of the following: onhold, bought, canceled, sold");
        }
    }
}
