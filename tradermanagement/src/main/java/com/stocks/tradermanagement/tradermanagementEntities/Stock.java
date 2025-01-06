package com.stocks.tradermanagement.tradermanagementEntities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "stocks")
public class Stock {
    @Id
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