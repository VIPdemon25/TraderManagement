package com.stocks.tradermanagement.tradermanagementDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchlistDTO {
    private Long id; // Unique identifier for the watchlist
    private String accountId; // Associated account
    private List<String> stockIds; // List of stock IDs in the watchlist
}
