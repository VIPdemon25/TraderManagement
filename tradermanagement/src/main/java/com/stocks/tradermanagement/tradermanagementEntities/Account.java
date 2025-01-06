package com.stocks.tradermanagement.tradermanagementEntities;

import java.util.List;

import org.springframework.data.annotation.Id;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
 
 
@Data
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @Column(unique = true , nullable = false)
    private String accountId;
    private String Fname;
    private String Lname;
    @Column(unique = true , nullable = false)
    private String email;
    private boolean status;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Holdings> holdings;
}