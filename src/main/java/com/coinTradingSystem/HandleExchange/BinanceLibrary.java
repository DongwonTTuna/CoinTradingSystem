/* ERR

package com.coinTradingSystem.HandleExchange;

import com.binance.api.client.BinanceApiAsyncRestClient;
import com.binance.api.client.BinanceApiClientFactory;
import com.coinTradingSystem.Main;

import java.util.HashMap;

public class BinanceLibrary {

    public BinanceApiAsyncRestClient client;

    public BinanceLibrary(){
        HashMap<String,String> API = Main.getOneAPI("BINANCE");
        BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(API.get("apikey"),API.get("secretkey"));
        client = factory.newAsyncRestClient();
    }

}
*/