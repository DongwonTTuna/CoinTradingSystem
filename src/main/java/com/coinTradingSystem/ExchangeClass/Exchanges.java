package com.coinTradingSystem.ExchangeClass;

import com.coinTradingSystem.Main;
import com.coinTradingSystem.CoreController.CoreController;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class Exchanges extends ExchangeFunctions{

    public void initialize( String ExClassName){
        CompletableFuture.runAsync(()-> {
            HashMap<String, String> API = Main.getOneAPI(Main.CurrentExchange);
            if (API.get("apikey") == null || API.get("secretkey") == null) {this.isAPINone = "0";Main.isExchangeLoaded = false; return;}
            this.isAPINone = "1";
            Main.isExchangeLoaded = true;
            super.exchange = ExchangeFactory.INSTANCE.createExchangeWithoutSpecification(ExClassName);
            ExchangeSpecification spec = exchange.getDefaultExchangeSpecification();
            spec.setApiKey(API.get("apikey"));
            spec.setSecretKey(API.get("secretkey"));
            exchange.applySpecification(spec);
            CompletableFuture.runAsync(()-> accountService =  exchange.getAccountService()).thenRunAsync(this::getWalletInfo);
            CompletableFuture.runAsync(()-> marketDataService = exchange.getMarketDataService()).thenRunAsync(this::InitializeTickersAndPrice);
            CompletableFuture.runAsync(()->  tradeService = exchange.getTradeService());
            this.initializeDone = true;
        });
    }

    public Exchanges(String ClassName, CoreController coreController){
        initialize(ClassName);
        core = coreController;
    }
}
