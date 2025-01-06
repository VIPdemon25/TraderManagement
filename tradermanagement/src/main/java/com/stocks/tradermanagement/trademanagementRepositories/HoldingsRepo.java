package com.stocks.tradermanagement.trademanagementRepositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stocks.tradermanagement.tradermanagementEntities.Holdings;

public interface HoldingsRepo extends JpaRepository<Holdings, String> {
	List<Holdings> findAllByAccountId(String accountId);
}
