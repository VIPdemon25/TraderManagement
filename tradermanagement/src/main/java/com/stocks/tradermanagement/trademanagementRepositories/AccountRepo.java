package com.stocks.tradermanagement.trademanagementRepositories;
import com.stocks.tradermanagement.tradermanagementEntities.Account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepo extends JpaRepository<Account, String>{

}
