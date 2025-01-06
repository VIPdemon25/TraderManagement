package com.stocks.tradermanagement.tradermanagementDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockDTO {
    private int stockId;
    private String name;
    private String symbol;
    private int totalShares;
    private double open;
    private double last;
    private boolean status;
    private String type;
    private LocalDate date;
} 