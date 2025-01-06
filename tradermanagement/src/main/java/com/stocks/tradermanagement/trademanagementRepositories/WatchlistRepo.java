package com.stocks.tradermanagement.trademanagementRepositories;

import com.stocks.tradermanagement.tradermanagementEntities.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchlistRepo extends JpaRepository<Watchlist, Long> {
    Watchlist findByAccountId(String accountId);
}
