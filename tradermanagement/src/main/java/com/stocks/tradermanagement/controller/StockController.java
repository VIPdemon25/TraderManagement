package com.stocks.tradermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.stocks.tradermanagement.tradermanagementDTOs.StockDTO;
import com.stocks.tradermanagement.tradermanagementEntities.Stock;
import com.stocks.tradermanagement.trademanagementRepositories.StockRepo;
import org.modelmapper.ModelMapper;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    @Autowired
    private StockRepo stockRepo;
    
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/receive")
    public ResponseEntity<String> receiveStock(@RequestBody StockDTO stockDTO) {
        try {
            Stock stock = modelMapper.map(stockDTO, Stock.class);
            stockRepo.save(stock);
            return ResponseEntity.ok("Stock data received and saved successfully. Stock ID: " + stock.getStockId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error saving stock data: " + e.getMessage());
        }
    }

    @GetMapping("/{stockId}")
    public ResponseEntity<StockDTO> getStock(@PathVariable int stockId) {
        return stockRepo.findById(stockId)
            .map(stock -> ResponseEntity.ok(modelMapper.map(stock, StockDTO.class)))
            .orElse(ResponseEntity.notFound().build());
    }
} 