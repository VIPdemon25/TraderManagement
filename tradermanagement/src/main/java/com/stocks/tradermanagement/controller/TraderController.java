package com.stocks.tradermanagement.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stocks.tradermanagement.tradermanagementDTOs.AccountDTO;
import com.stocks.tradermanagement.tradermanagementDTOs.TradeDTO;
import com.stocks.tradermanagement.tradermanagementService.AccountService;
import com.stocks.tradermanagement.tradermanagementService.TradeService;


@RestController
@RequestMapping
public class TraderController 

{       
    @Autowired
    private AccountService accounService;

    @Autowired
    private TradeService tradeService;

    @PostMapping("/receiveAccount")
    public ResponseEntity<AccountDTO> getAccount(@RequestBody AccountDTO accounDto)
    {
    	accounService.registerAccount(accounDto);
		return new ResponseEntity<>(HttpStatus.OK);
    	
    }
    @PostMapping("/updateAccount")
    public ResponseEntity<AccountDTO> updateAccount(@RequestBody AccountDTO accounDto)
    {
    	accounService.registerAccount(accounDto);
		return new ResponseEntity<>(HttpStatus.OK);
    	
    }
    
    @GetMapping("/trades/pull")
    public ResponseEntity<List<TradeDTO>> getLastFiveDaysTrades(
            @RequestParam(required = false) String accountId,
            @RequestParam(required = false) Integer stockId) {
        return ResponseEntity.ok(tradeService.getLastFiveDaysTrades(accountId, stockId));
    }

    @PostMapping("/trades/receive")
    public ResponseEntity<String> receiveTrade(@RequestBody TradeDTO tradeDTO) {
        return ResponseEntity.ok(tradeService.saveTrade(tradeDTO));
    }

    // Add this at class level
    @SuppressWarnings("unused")
    private static final List<TradeDTO> trades = new ArrayList<>();
}
