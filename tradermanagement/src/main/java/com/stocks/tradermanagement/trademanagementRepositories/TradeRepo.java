package com.stocks.tradermanagement.trademanagementRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.stocks.tradermanagement.tradermanagementEntities.Trade;
import java.time.LocalDateTime;
import java.util.List;

public interface TradeRepo extends JpaRepository<Trade, Long> {
    @Query("SELECT t FROM Trade t WHERE t.dtime >= ?1 AND (?2 IS NULL OR t.accountId = ?2) AND (?3 IS NULL OR t.stockId = ?3)")
    List<Trade> findLastNDaysTrades(LocalDateTime fromDate, String accountId, Integer stockId);
} 