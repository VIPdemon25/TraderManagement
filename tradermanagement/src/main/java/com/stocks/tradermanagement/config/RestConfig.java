package com.stocks.tradermanagement.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
