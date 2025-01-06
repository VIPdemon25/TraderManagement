package com.stocks.tradermanagement.tradermanagementEntities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "trades")
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tradeId;
    
    private String accountId;
    private String symbol;
    private int stockId;
    private int numShares;
    private double tradedAt;
    private LocalDateTime dtime;
    private String transType;
    private String typeofPurchase;
    private String typeofsell;
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "accountId", insertable = false, updatable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "stockId", insertable = false, updatable = false)
    private Stock stock;
} 