package com.coinTradingSystem.UI.MainFrame.Tab.EventHandler;

import com.coinTradingSystem.CoreController.CoreController;
import com.coinTradingSystem.Main;
import com.coinTradingSystem.SqlQuery;
import com.coinTradingSystem.UI.MainFrame.MainFrame;
import javafx.application.Platform;

import java.util.concurrent.CompletableFuture;

public class StatusTabEventHandler {
    public MainFrame mainFrame;
    
    public void UpdateOpenOrderNum(){
        CompletableFuture.supplyAsync(()-> SqlQuery.getOrderList(Main.Exchange.getExchangeName())).thenAcceptAsync((s)->{
            Platform.runLater(()->mainFrame.OpenOrders.setText("" + s.size()));
        });
    }
}
