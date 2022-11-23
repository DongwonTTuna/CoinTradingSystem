package com.coinTradingSystem.CoreController.TradeSystem;

import com.coinTradingSystem.CoreController.CoreController;

import java.util.concurrent.CompletableFuture;

import static org.awaitility.Awaitility.await;

public class TradeCore {

    private final CoreController core;
    public FetchOrder Forders;
    private ProcessOrder POrder;

    public TradeCore(CoreController coreController){
        core = coreController;
        Forders = new FetchOrder();

        CompletableFuture.runAsync(()->{core.callBackFunctions.WaitTilAlltickerUpdated();}).thenRunAsync(()-> POrder = new ProcessOrder(core,Forders));
    }
}
