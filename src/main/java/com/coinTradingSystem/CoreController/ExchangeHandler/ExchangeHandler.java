package com.coinTradingSystem.CoreController.ExchangeHandler;


import com.coinTradingSystem.CoreController.ExchangeHandler.ExchangeClass.Exchanges;
import com.coinTradingSystem.Main;
import com.coinTradingSystem.CoreController.CoreController;
import com.coinTradingSystem.CoreController.addEventOnAction;
import javafx.application.Platform;
import org.knowm.xchange.binance.BinanceExchange;
import org.knowm.xchange.gateio.GateioExchange;
import org.knowm.xchange.upbit.UpbitExchange;

import java.util.concurrent.CompletableFuture;

public class ExchangeHandler extends ExchangeTabFunctions implements addEventOnAction {

    private void ExchangeSetup() {
        coreController.mainFrame.ConnectionLog.setText(Main.CurrentExchange + "に接続中");
        switch (Main.CurrentExchange) {
            case "BINANCE" -> {exchange = new Exchanges(BinanceExchange.class.getName(),coreController);coreController.mainFrame.OrderWorthInUsd.setText("Worth In USDT");coreController.mainFrame.BalanceWorthInUSD.setText("Worth In USDT");}
            case "GATEIO" -> {exchange = new Exchanges(GateioExchange.class.getName(),coreController);coreController.mainFrame.OrderWorthInUsd.setText("Worth In USDT");coreController.mainFrame.BalanceWorthInUSD.setText("Worth In USDT");}
            case "UPBIT" -> {exchange = new Exchanges(UpbitExchange.class.getName(),coreController);coreController.mainFrame.OrderWorthInUsd.setText("Worth In KRW");coreController.mainFrame.BalanceWorthInUSD.setText("Worth In KRW");}
        }
        ConnectionLogUpdate();
        SetupTheTab(coreController.mainFrame.InfoTab.getSelectionModel().getSelectedItem().getText(),coreController);
    }
    private void ConnectionLogUpdate(){
        CompletableFuture.runAsync(() -> {
            try {
                coreController.callBackFunctions.WaitTilExchangeOnlined();
                Platform.runLater(() -> coreController.mainFrame.ConnectionLog.setText("接続が取れました"));
            } catch (Exception e) {
                Platform.runLater(() -> coreController.mainFrame.ConnectionLog.setText("接続が取れませんでした"));
            }
        });
    }

    public void ExchangeFunction() {
        coreController.controlValue.InitializeAllValue();
        ExchangeSetup();
        coreController.controlValue.AddLog("初期化が完了しました");
    }

    public ExchangeHandler(CoreController coreController) {
        super.coreController = coreController;
    }
}
