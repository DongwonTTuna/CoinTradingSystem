package com.coinTradingSystem.CoreController.TradeSystem;

import com.coinTradingSystem.Main;
import com.coinTradingSystem.SqlQuery;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class FetchOrder {
    public ArrayList<ArrayList<String>> orderList;
    public FetchOrder(){
        CompletableFuture.runAsync(()->{
            while(true){
                orderList = SqlQuery.getOrderList(Main.CurrentExchange);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
