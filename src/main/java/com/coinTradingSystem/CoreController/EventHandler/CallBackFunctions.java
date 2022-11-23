package com.coinTradingSystem.CoreController.EventHandler;

import com.coinTradingSystem.CoreController.CoreController;

import static org.awaitility.Awaitility.await;

public class CallBackFunctions {
    private final CoreController coreController;
    public void WaitTilExchangeTickersUpdated(){
        await().until(()-> coreController.exchangeHandler.exchange.AllTickers != null);
    }
    public void WaitTilExchangeOnlined(){
        await().until(()-> coreController.exchangeHandler.exchange.accountService != null);
    }
    public void WaitTilAccountIsNotNull(){
        await().until(()-> coreController.exchangeHandler.exchange.accountService != null);
    }
    public void WaitTilFetchAPI(){
        await().until(()->coreController.exchangeHandler.exchange.isAPINone != null);
    }
    public void WaitTilAccountBalanceInitialized(){
        await().until(()->coreController.exchangeHandler.exchange.UserAccountBalance != null);
    }
    public void WaitTilExchangeInitialized(){
        await().until(()->coreController.exchangeHandler.exchange != null);
    }
    public void WaitTilTradeServiceOnlined(){
        await().until(()->coreController.exchangeHandler.exchange.tradeService != null);
    }
    public void WaitTilAlltickerUpdated(){
        await().until(()->coreController.exchangeHandler.exchange.AllTickerWithInstrument != null);
    }
    public CallBackFunctions(CoreController coreController){
        this.coreController = coreController;
    }

}
