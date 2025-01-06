package com.stocks.tradermanagement.tradermanagementService;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.stocks.tradermanagement.tradermanagementDTOs.AccountDTO;
import com.stocks.tradermanagement.tradermanagementDTOs.HoldingsDTO;
import com.stocks.tradermanagement.tradermanagementEntities.Account;
import com.stocks.tradermanagement.tradermanagementEntities.Holdings;
import com.stocks.tradermanagement.trademanagementRepositories.AccountRepo;
import com.stocks.tradermanagement.exceptions.StockAlreadySoldException;
import com.stocks.tradermanagement.exceptions.AccountIdDontOwnTheStock;
import com.stocks.tradermanagement.trademanagementRepositories.HoldingsRepo;

@Service
public class HoldingsService {

    @Autowired
    private HoldingsRepo holdingsRepo;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private ModelMapper modelMapper;

    public void registerTrader(AccountDTO accountDTO) {
        Account account = modelMapper.map(accountDTO, Account.class);
        accountRepo.save(account);
    }

    public void createHoldings(HoldingsDTO holdingsDTO) {
        Holdings holdings = modelMapper.map(holdingsDTO, Holdings.class);
        
        // Ensure enough balance in the account
        if (holdings.getBalance() < holdings.getEntryPrice() * holdings.getNumShares()) {
            throw new IllegalArgumentException("Not enough balance to create new holdings");
        }
        // Ensure stopLoss is not negative
        if (holdings.getStopLoss() < 0) {
            throw new IllegalArgumentException("StopLoss cannot be negative");
        }
        // Set NumShares to zero if typeOfPurchase is PositionSizing
        if ("PositionSizing".equalsIgnoreCase(holdings.getTypeOfPurchase())) {
            holdings.setNumShares(0);
        }
        holdingsRepo.save(holdings);
    }

    public void deleteHoldings(String holdingId) {
        if (!holdingsRepo.existsById(holdingId)) {
            throw new IllegalArgumentException("Holding not found");
        }
        holdingsRepo.deleteById(holdingId);
    }

    // Duplicate method removed
        Holdings holdings = modelMapper.map(holdingsDTO, Holdings.class);
        
        // Ensure enough balance in the account
        if (holdings.getBalance() < holdings.getEntryPrice() * holdings.getNumShares()) {
            throw new IllegalArgumentException("Not enough balance to buy new holdings");
        }
        // Ensure stopLoss is not negative
        if (holdings.getStopLoss() < 0) {
            throw new IllegalArgumentException("StopLoss cannot be negative");
        }
        // Set NumShares to zero if typeOfPurchase is PositionSizing
        if ("PositionSizing".equalsIgnoreCase(holdings.getTypeOfPurchase())) {
            holdings.setNumShares(0);
        }
        holdingsRepo.save(holdings);
    }

    public void updateHoldings(HoldingsDTO holdingsDTO) {
        Holdings holdings = modelMapper.map(holdingsDTO, Holdings.class);
        
        // Ensure stopLoss is not negative
        if (holdings.getStopLoss() < 0) {
            throw new IllegalArgumentException("StopLoss cannot be negative");
        }
        // Set stopLoss to zero if typeOfSell is marketPrice
        if ("marketPrice".equalsIgnoreCase(holdings.getTypeOfSell())) {
            holdings.setStopLoss(0);
        }
        holdingsRepo.save(holdings);
    }

    public List<HoldingsDTO> getHoldingsByAccountId(String accountId) {
        List<Holdings> holdingsList = holdingsRepo.findAllByAccountId(accountId);
        return holdingsList.stream()
                .map(holdings -> modelMapper.map(holdings, HoldingsDTO.class))
                .collect(Collectors.toList());
    }

    public void checkStockStatus(String accountId, String stockId) {
        List<Holdings> holdingsList = holdingsRepo.findAllByAccountId(accountId);
        boolean stockFound = false;

        for (Holdings holding : holdingsList) {
            if (holding.getStockId().equals(stockId)) {
                stockFound = true;
                if (holding.getNumShares() <= 0) {
                    throw new StockAlreadySoldException("The stock has already been sold.");
                }
                return;
            }
        }

        if (!stockFound) {
            throw new AccountIdDontOwnTheStock("Account ID does not own the specified stock.");
        }
    }
}
