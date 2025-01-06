package com.stocks.tradermanagement.tradermanagementEntities;

import org.springframework.data.annotation.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "watchlist")
public class Watchlist {
    @Id
    private Long id; // Unique identifier for the watchlist
    private String accountId; // Associated account
    private List<Integer> stockIds; // List of stock IDs in the watchlist
}
