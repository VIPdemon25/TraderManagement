package com.stocks.tradermanagement.trademanagementRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.stocks.tradermanagement.tradermanagementEntities.Stock;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface StockRepo extends JpaRepository<Stock, Integer> {
    @Query(value = "SELECT * FROM stocks WHERE stock_id = ?1 ORDER BY date DESC LIMIT ?2", 
           nativeQuery = true)
    List<Stock> findLastNDaysByStockId(int stockId, int days);
} 