package com.coinTradingSystem.CoreController.Exchange.OrderThread;

import com.coinTradingSystem.Main;
import com.coinTradingSystem.SqlQuery;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class FetchOrder {
    public ArrayList<ArrayList<String>> orderList;
    public FetchOrder(){
        CompletableFuture.runAsync(()->{
            while(true){
                orderList = SqlQuery.getOrderList(Main.Exchange.getExchangeName());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
