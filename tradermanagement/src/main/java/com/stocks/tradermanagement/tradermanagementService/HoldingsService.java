package com.stocks.tradermanagement.tradermanagementService;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stocks.tradermanagement.exceptions.AccountIdDontOwnTheStock;
import com.stocks.tradermanagement.exceptions.StockAlreadySoldException;
import com.stocks.tradermanagement.trademanagementRepositories.AccountRepo;
import com.stocks.tradermanagement.trademanagementRepositories.HoldingsRepo;
import com.stocks.tradermanagement.tradermanagementDTOs.AccountDTO;
import com.stocks.tradermanagement.tradermanagementDTOs.HoldingsDTO;
import com.stocks.tradermanagement.tradermanagementDTOs.TransactionStatusDTO;
import com.stocks.tradermanagement.tradermanagementEntities.Account;
import com.stocks.tradermanagement.tradermanagementEntities.Holdings;

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
        
        // Validate the creation
        if (holdings.getNumShares() <= 0) {
            throw new IllegalArgumentException("Number of shares must be positive");
        }
        
        // Ensure enough balance in the account
        double purchaseAmount = holdings.getStock().getOpen() * holdings.getNumShares();
        if (holdings.getBalance() < purchaseAmount) {
            throw new IllegalArgumentException("Not enough balance to create new holdings. Required: " + 
                purchaseAmount + ", Available: " + holdings.getBalance());
        }
        
        // Ensure stopLoss is not negative and less than entry price
        if (holdings.getStopLoss() < 0) {
            throw new IllegalArgumentException("StopLoss cannot be negative");
        }
        if (holdings.getStopLoss() >= holdings.getStock().getOpen()) {
            throw new IllegalArgumentException("StopLoss must be less than entry price");
        }
        
        // Set initial values
        holdings.setBoughtAt(holdings.getStock().getOpen());
        holdings.setBalance(holdings.getBalance() - purchaseAmount);
        holdings.setStatus("bought");
        
        // Save the holding (ID will be generated automatically)
        holdingsRepo.save(holdings);
    }

    public void deleteHoldings(String holdingId) {
        if (!holdingsRepo.existsById(holdingId)) {
            throw new IllegalArgumentException("Holding not found");
        }
        holdingsRepo.deleteById(holdingId);
    }

    public TransactionStatusDTO updateHoldings(HoldingsDTO holdingsDTO) {
        Holdings holdings = modelMapper.map(holdingsDTO, Holdings.class);
        TransactionStatusDTO transactionStatus = new TransactionStatusDTO();
        
        // For selling operations
        if (holdings.getTypeOfSell() != null) {
            // Get existing holding to check current shares
            Holdings existingHolding = holdingsRepo.findById(holdings.getHoldingId())
                .orElseThrow(() -> new IllegalArgumentException("Holding not found"));

            String status = existingHolding.getStatus().toLowerCase();
            if (status.equals("cancelled") || status.equals("onhold") || status.equals("sold")) {
                throw new IllegalStateException("Cannot sell holdings with status: " + status);
            }
            if (!status.equals("bought")) {
                throw new IllegalStateException("Can only sell holdings with 'bought' status");
            }
            
            if (existingHolding.getNumShares() < holdings.getNumShares()) {
                throw new IllegalArgumentException("Cannot sell more shares than owned. Available: " + existingHolding.getNumShares());
            }
            
            // Update number of shares and balance after selling using last price
            int sharesToSell = holdings.getNumShares();
            double sellAmount = holdings.getStock().getLast() * sharesToSell;
            
            // Update the existing holding
            existingHolding.setNumShares(existingHolding.getNumShares() - sharesToSell);
            existingHolding.setBalance(existingHolding.getBalance() + sellAmount);
            existingHolding.setSoldAt(holdings.getStock().getLast());
            
            if (existingHolding.getNumShares() == 0) {
                existingHolding.setStatus("sold");
            }
            
            holdingsRepo.save(existingHolding);
            
            transactionStatus.setStatus("SUCCESS");
            transactionStatus.setSharesTraded(sharesToSell);
            transactionStatus.setPrice(holdings.getStock().getLast());
            transactionStatus.setTransactionType("SELL");
            transactionStatus.setMessage("Successfully sold " + sharesToSell + " shares at " + holdings.getStock().getLast());
            
            return transactionStatus;
        }
        
        // For buying operations
        if (holdings.getTypeOfPurchase() != null) {
            int sharesToBuy = holdings.getNumShares();
            double purchaseAmount = holdings.getStock().getOpen() * sharesToBuy;
            
            // Check if it's updating an existing holding
            if (holdings.getHoldingId() != null) {
                // Update existing holding
                Holdings existingHolding = holdingsRepo.findById(holdings.getHoldingId())
                    .orElseThrow(() -> new IllegalArgumentException("Holding not found"));
                
                // Verify it's the same stock
                if (existingHolding.getStockId() != holdings.getStockId()) {
                    throw new IllegalArgumentException("Cannot update holding with different stock ID");
                }
                
                // Verify status is 'bought'
                if (!existingHolding.getStatus().toLowerCase().equals("bought")) {
                    throw new IllegalStateException("Can only update holdings with 'bought' status");
                }
                
                if (existingHolding.getBalance() < purchaseAmount) {
                    throw new IllegalArgumentException("Insufficient balance for purchase. Required: " + 
                        purchaseAmount + ", Available: " + existingHolding.getBalance());
                }
                
                // Update existing holding
                existingHolding.setNumShares(existingHolding.getNumShares() + sharesToBuy);
                existingHolding.setBalance(existingHolding.getBalance() - purchaseAmount);
                existingHolding.setBoughtAt(holdings.getStock().getOpen());  // Update latest buy price
                
                holdingsRepo.save(existingHolding);
            } else {
                // Create new holding
                if (holdings.getBalance() < purchaseAmount) {
                    throw new IllegalArgumentException("Insufficient balance for purchase. Required: " + 
                        purchaseAmount + ", Available: " + holdings.getBalance());
                }
                
                holdings.setBalance(holdings.getBalance() - purchaseAmount);
                holdings.setBoughtAt(holdings.getStock().getOpen());
                holdings.setStatus("bought");
                
                holdingsRepo.save(holdings);
            }
            
            transactionStatus.setStatus("SUCCESS");
            transactionStatus.setSharesTraded(sharesToBuy);
            transactionStatus.setPrice(holdings.getStock().getOpen());
            transactionStatus.setTransactionType("BUY");
            transactionStatus.setMessage("Successfully bought " + sharesToBuy + " shares at " + holdings.getStock().getOpen());
            
            return transactionStatus;
        }
        
        // Common validations
        if (holdings.getStopLoss() < 0) {
            throw new IllegalArgumentException("StopLoss cannot be negative");
        }
        if ("marketPrice".equalsIgnoreCase(holdings.getTypeOfSell())) {
            holdings.setStopLoss(0);
        }
        
        holdingsRepo.save(holdings);
        
        transactionStatus.setStatus("SUCCESS");
        transactionStatus.setMessage("Holdings updated successfully");
        return transactionStatus;
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
            if (String.valueOf(holding.getStockId()).equals(stockId)) {
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

    public TransactionStatusDTO updateBalance(String holdingId, double newBalance) {
        TransactionStatusDTO transactionStatus = new TransactionStatusDTO();
        
        Holdings holdings = holdingsRepo.findById(holdingId)
            .orElseThrow(() -> new IllegalArgumentException("Holding not found with ID: " + holdingId));
        
        // Validate new balance is not negative
        if (newBalance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        
        // Update the balance
        double oldBalance = holdings.getBalance();
        holdings.setBalance(newBalance);
        holdingsRepo.save(holdings);
        
        // Prepare response
        transactionStatus.setStatus("SUCCESS");
        transactionStatus.setTransactionType("BALANCE_UPDATE");
        transactionStatus.setMessage("Balance updated successfully from " + oldBalance + " to " + newBalance);
        
        return transactionStatus;
    }

    public TransactionStatusDTO sellAtStopLoss(String holdingId) {
        TransactionStatusDTO transactionStatus = new TransactionStatusDTO();
        
        Holdings holdings = holdingsRepo.findById(holdingId)
            .orElseThrow(() -> new IllegalArgumentException("Holding not found with ID: " + holdingId));
            
        if (!holdings.getStatus().toLowerCase().equals("bought")) {
            throw new IllegalStateException("Can only sell holdings with 'bought' status");
        }
        
        if (holdings.getNumShares() <= 0) {
            throw new IllegalArgumentException("No shares available to sell");
        }
        
        // Sell all shares at stoploss price
        double sellAmount = holdings.getStopLoss() * holdings.getNumShares();
        double originalShares = holdings.getNumShares();
        
        holdings.setBalance(holdings.getBalance() + sellAmount);
        holdings.setNumShares(0);
        holdings.setSoldAt(holdings.getStopLoss());
        holdings.setStatus("sold");
        
        holdingsRepo.save(holdings);
        
        // Prepare response
        transactionStatus.setStatus("SUCCESS");
        transactionStatus.setSharesTraded((int) originalShares);
        transactionStatus.setPrice(holdings.getStopLoss());
        transactionStatus.setTransactionType("SELL_STOPLOSS");
        transactionStatus.setMessage("Successfully sold " + originalShares + " shares at stoploss price: " + holdings.getStopLoss());
        
        return transactionStatus;
    }
}
