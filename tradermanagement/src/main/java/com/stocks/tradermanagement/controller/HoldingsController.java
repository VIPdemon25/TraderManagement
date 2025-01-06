package com.stocks.tradermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.stocks.tradermanagement.tradermanagementDTOs.HoldingsDTO;
import com.stocks.tradermanagement.tradermanagementDTOs.TransactionStatusDTO;
import com.stocks.tradermanagement.tradermanagementService.HoldingsService;
import com.stocks.tradermanagement.tradermanagementEntities.Stock;
import com.stocks.tradermanagement.tradermanagementDTOs.StockDTO;
import com.stocks.tradermanagement.trademanagementRepositories.StockRepo;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;

@RestController
@RequestMapping("/api/holdings")
public class HoldingsController {

    @Autowired
    private HoldingsService holdingsService;

    @Autowired
    private StockRepo stockRepo;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<TransactionStatusDTO> createHolding(@RequestBody HoldingsDTO holdingsDTO) {
        // Set initial status and purchase type
        holdingsDTO.setStatus("bought");
        holdingsDTO.setTypeOfPurchase("initial");
        
        // Create the holding and perform initial purchase
        holdingsService.createHoldings(holdingsDTO);
        
        // Perform the buy operation
        holdingsDTO.setTypeOfPurchase("buy");
        TransactionStatusDTO status = holdingsService.updateHoldings(holdingsDTO);
        
        return ResponseEntity.ok(status);
    }

    @DeleteMapping("/{holdingId}")
    public ResponseEntity<TransactionStatusDTO> deleteHolding(@PathVariable String holdingId) {
        // First sell all remaining shares
        TransactionStatusDTO sellStatus = holdingsService.sellAtStopLoss(holdingId);
        
        // Then delete the holding
        holdingsService.deleteHoldings(holdingId);
        
        return ResponseEntity.ok(sellStatus);
    }

    @PutMapping("/buy")
    public ResponseEntity<TransactionStatusDTO> buyShares(@RequestBody HoldingsDTO holdingsDTO) {
        holdingsDTO.setTypeOfPurchase("buy");  // Ensure it's a buy operation
        TransactionStatusDTO status = holdingsService.updateHoldings(holdingsDTO);
        return ResponseEntity.ok(status);
    }

    @PutMapping("/sell")
    public ResponseEntity<TransactionStatusDTO> sellShares(@RequestBody HoldingsDTO holdingsDTO) {
        holdingsDTO.setTypeOfSell("sell");  // Ensure it's a sell operation
        TransactionStatusDTO status = holdingsService.updateHoldings(holdingsDTO);
        return ResponseEntity.ok(status);
    }

    @PutMapping("/sell-stoploss/{holdingId}")
    public ResponseEntity<TransactionStatusDTO> sellAtStopLoss(@PathVariable String holdingId) {
        TransactionStatusDTO status = holdingsService.sellAtStopLoss(holdingId);
        return ResponseEntity.ok(status);
    }

    @PutMapping("/update-balance/{holdingId}")
    public ResponseEntity<TransactionStatusDTO> updateBalance(
            @PathVariable String holdingId,
            @RequestParam double newBalance) {
        TransactionStatusDTO status = holdingsService.updateBalance(holdingId, newBalance);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<HoldingsDTO>> getHoldingsByAccount(@PathVariable String accountId) {
        List<HoldingsDTO> holdings = holdingsService.getHoldingsByAccountId(accountId);
        return ResponseEntity.ok(holdings);
    }

    @GetMapping("/check-stock")
    public ResponseEntity<Void> checkStockStatus(
            @RequestParam String accountId,
            @RequestParam String stockId) {
        holdingsService.checkStockStatus(accountId, stockId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/watchlist/{stockId}")
    public ResponseEntity<List<StockDTO>> getStockHistory(
            @PathVariable int stockId,
            @RequestParam(defaultValue = "5") int days) {
        List<Stock> stockHistory = stockRepo.findLastNDaysByStockId(stockId, days);
        List<StockDTO> stockDTOs = stockHistory.stream()
            .map(stock -> modelMapper.map(stock, StockDTO.class))
            .collect(Collectors.toList());
        return ResponseEntity.ok(stockDTOs);
    }
} 