package com.coinTradingSystem.UI.MainFrame.CoreController.ExchangeHandler;

import com.coinTradingSystem.HandleExchange.Exchanges;
import com.coinTradingSystem.UI.MainFrame.CoreController.CoreController;
import javafx.application.Platform;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class ExchangeTabFunctions {
    public CoreController coreController;
    public Exchanges exchange;

    public void UpdateStatusTabVariables() {
        if (exchange.getExchangeStatus())
            Platform.runLater(() -> coreController.mainFrame.ExchangeStatus.setText("OK"));
        else Platform.runLater(() -> coreController.mainFrame.ExchangeStatus.setText("IN MAINTENCE"));
        CompletableFuture.runAsync(() -> coreController.callBackFunctions.WaitTilExchangeTickersUpdated())
                .thenRunAsync(() -> coreController.exchangeHandler.exchange.getUserAccountBalance())
                .thenRunAsync(()-> {
                    for (HashMap<String, String> item : coreController.exchangeHandler.exchange.UserAccountBalance) {
                        item.get("symbol");
                    }
                });

    }

    public void UpdateBalanceTabVariables() {
        System.out.println("y");

    }

    public void UpdateOrdersTabVariables() {
        System.out.println("a");

    }

}
