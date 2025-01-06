package com.stocks.tradermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stocks.tradermanagement.tradermanagementDTOs.AccountDTO;
import com.stocks.tradermanagement.tradermanagementService.AccountService;


@RestController
@RequestMapping
public class TraderController 

{       
    @Autowired
    private AccountService accounService;

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
    
    

}
