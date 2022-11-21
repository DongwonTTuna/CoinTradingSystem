package com.coinTradingSystem.UI.MainFrame.CoreController.EventHandler;

import com.coinTradingSystem.UI.MainFrame.CoreController.CoreController;

import java.util.concurrent.Callable;

import static org.awaitility.Awaitility.await;

public class CallBackFunctions {
    private final CoreController coreController;
    public void WaitTilExchangeInitializeDone(){
       await().until(() -> coreController.exchangeHandler.exchange.initializeDone);
    }
    public void WaitTilExchangeTickersUpdated(){
        await().until(()-> coreController.exchangeHandler.exchange.AllTickers != null);
    }
    public CallBackFunctions(CoreController coreController){
        this.coreController = coreController;
    }

}
