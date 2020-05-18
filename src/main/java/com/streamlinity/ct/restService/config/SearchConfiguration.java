package com.streamlinity.ct.restService.config;

import com.streamlinity.ct.restService.challenge.SearchSvcImpl;
import com.streamlinity.ct.restService.challenge.SearchSvcInterface;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("default")
public class SearchConfiguration {
    @Bean
    public SearchSvcInterface candSearch() {
        return new SearchSvcImpl();
    }
}

