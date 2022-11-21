package com.coinTradingSystem.UI.MainFrame.CoreController.Variables;

import com.coinTradingSystem.Main;
import com.coinTradingSystem.SqlQuery;
import com.coinTradingSystem.UI.MainFrame.CoreController.CoreController;
import javafx.application.Platform;
import javafx.scene.control.Label;

import java.util.concurrent.CompletableFuture;

public class ControlValue {
    private CoreController coreController;

    public void UpdateOCO(){
        CompletableFuture.supplyAsync(()-> SqlQuery.getOrderList(Main.CurrentExchange)).thenAcceptAsync((s)->{
            Platform.runLater(()->coreController.mainFrame.OpenOrders.setText("" + s.size()));
        });
    }

    public void AddLog(String msg){
        Platform.runLater(()->coreController.mainFrame.LogBox.getChildren().add(new Label(msg)));
    }

    public void InitializeAllValue(){
        coreController.mainFrame.ExchangeStatus.setText("Fetching...");
        coreController.mainFrame.TotalBalance.setText("Fetching...");
        coreController.mainFrame.OpenOrders.setText("Fetching...");
        coreController.mainFrame.TotalTrades.setText("Fetching...");
        coreController.mainFrame.TodayProfit.setText("Fetching...");
        coreController.mainFrame.BalanceTable.getItems().clear();
        coreController.mainFrame.TB = 0.0;
        coreController.mainFrame.TotalBalanceInUSD.setText("0");
        coreController.mainFrame.LogBox.getChildren().clear();
    }


    public ControlValue(CoreController coreController){
        this.coreController = coreController;
    }

    public void InitializeOnStart(){
        CompletableFuture.runAsync(() -> coreController.callBackFunctions.WaitTilExchangeInitializeDone()).thenAcceptAsync((s) -> coreController.exchangeHandler.UpdateStatusTabVariables());
    }
}
