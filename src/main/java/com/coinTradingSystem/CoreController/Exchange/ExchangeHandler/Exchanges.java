package com.coinTradingSystem.CoreController.Exchange.ExchangeHandler;

import com.coinTradingSystem.CoreController.Exchange.ExchangeHandler.ExchangeFunctions;
import com.coinTradingSystem.CoreController.Exchange.ExchangeMain;
import com.coinTradingSystem.Main;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class Exchanges extends ExchangeFunctions {


    private HashMap<String, String> fetchAPIKEY(){
        HashMap<String, String> API = Main.getOneAPI(ExchangeName);
        if (API.get("apikey") == null || API.get("secretkey") == null) {this.isAPINone = "-1";Main.isExchangeLoaded = false; return null;}
        this.isAPINone = "1";
        Main.isExchangeLoaded = true;
        return API;
    }
    private void initializeExchange(String ExClassName){
        exchange = ExchangeFactory.INSTANCE.createExchangeWithoutSpecification(ExClassName);
        ExchangeSpecification spec = exchange.getDefaultExchangeSpecification();

        ExchangeName = spec.getExchangeName().toUpperCase();
        exData.updateBaseCurrency(Objects.equals(ExchangeName,"UPBIT")? "KRW" : "USDT");

        HashMap<String,String> API = fetchAPIKEY();
        if (API== null) return;
        spec.setApiKey(API.get("apikey"));
        spec.setSecretKey(API.get("secretkey"));

        exchange.applySpecification(spec);
    }

    public void initialize(String ExClassName){
        initializeExchange(ExClassName);
        if (Objects.equals(this.isAPINone, "-1")) return;
        CompletableFuture.runAsync(()-> {

            CompletableFuture.runAsync(()-> accountService =  exchange.getAccountService());
            CompletableFuture.runAsync(()-> marketDataService = exchange.getMarketDataService());
            CompletableFuture.runAsync(()-> tradeService = exchange.getTradeService());
        });
    }

    public Exchanges(String ClassName, ExchangeMain exchangeData){
        exData = exchangeData;
        initialize(ClassName);
        CompletableFuture.runAsync(this::runAll);
    }
}
