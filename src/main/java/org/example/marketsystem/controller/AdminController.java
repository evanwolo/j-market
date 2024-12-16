package org.example.marketsystem.controller;

import org.example.marketsystem.service.MarketEngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

private final MarketEngineService marketEngineService;

@Autowired
public AdminController(MarketEngineService marketEngineService) {
   this.marketEngineService = marketEngineService;
}


@PostMapping("/shutdownMarketReads")
public void shutdown(){
   marketEngineService.shutdown();
}




}
