package org.example.marketsystem;

import org.example.marketsystem.service.MarketEngineService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MarketSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarketSystemApplication.class, args);

    }

}
