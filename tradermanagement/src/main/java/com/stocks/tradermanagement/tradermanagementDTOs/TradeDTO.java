package com.stocks.tradermanagement.tradermanagementDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeDTO {
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
} 