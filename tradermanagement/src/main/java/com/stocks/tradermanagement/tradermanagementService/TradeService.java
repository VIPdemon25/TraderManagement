package com.stocks.tradermanagement.tradermanagementService;

import org.springframework.stereotype.Service;
import com.stocks.tradermanagement.tradermanagementDTOs.TradeDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class TradeService {
    private static final List<TradeDTO> trades = new ArrayList<>();

    public String saveTrade(TradeDTO tradeDTO) {
        trades.add(tradeDTO);
        return "Trade received successfully";
    }

    public List<TradeDTO> getLastFiveDaysTrades(String accountId, Integer stockId) {
        LocalDateTime fiveDaysAgo = LocalDateTime.now().minusDays(5);
        
        return trades.stream()
            .filter(trade -> trade.getDtime().isAfter(fiveDaysAgo))
            .filter(trade -> accountId == null || trade.getAccountId().equals(accountId))
            .filter(trade -> stockId == null || trade.getStockId() == stockId)
            .collect(Collectors.toList());
    }
} 