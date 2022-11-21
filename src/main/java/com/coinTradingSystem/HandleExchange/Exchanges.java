package com.coinTradingSystem.HandleExchange;

import com.coinTradingSystem.Main;
import com.coinTradingSystem.UI.MainFrame.CoreController.CoreController;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class Exchanges extends ExchangeFunctions{

    public void initialize( String ExClassName){
        CompletableFuture.runAsync(()-> {
            HashMap<String, String> API = Main.getOneAPI(Main.CurrentExchange);
            super.exchange = ExchangeFactory.INSTANCE.createExchangeWithoutSpecification(ExClassName);
            ExchangeSpecification spec = exchange.getDefaultExchangeSpecification();
            spec.setApiKey(API.get("apikey"));
            spec.setSecretKey(API.get("secretkey"));
            exchange.applySpecification(spec);
            accountService = exchange.getAccountService();
            marketDataService = exchange.getMarketDataService();
            tradeService = exchange.getTradeService();
            InitializeTickersAndPrice();
            this.initializeDone = true;
        });
    }

    public Exchanges(String ClassName, CoreController coreController){
        initialize(ClassName);
        core = coreController;
    }
}
