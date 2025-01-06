package com.stocks.tradermanagement.tradermanagementService;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.stocks.tradermanagement.trademanagementRepositories.AccountRepo;
import com.stocks.tradermanagement.tradermanagementDTOs.AccountDTO;
import com.stocks.tradermanagement.tradermanagementEntities.Account;

@Service
public class AccountService 
{ 

    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private ModelMapper modelMapper; 

    public ResponseEntity<Void> registerAccount(AccountDTO accountDTO)
    {
        Account account=modelMapper.map(accountDTO,Account.class);
        accountRepo.save(account);
        return ResponseEntity.ok().build();
        
    } 
public ResponseEntity<Void> updateAccount(AccountDTO accountDTO) {
    Optional<Account> existingAccount = accountRepo.findById(accountDTO.getAccountId());
    if (existingAccount.isPresent()) {
        modelMapper.map(accountDTO, existingAccount.get());
        accountRepo.save(existingAccount.get());
        return ResponseEntity.ok().build();
    } else {
        return ResponseEntity.notFound().build();
    }
}
    

}
