package com.coinTradingSystem.CoreController.Exchange.OrderThread;

import com.coinTradingSystem.CoreController.CoreController;
import com.coinTradingSystem.Main;

import java.util.concurrent.CompletableFuture;



public class TradeCore {

    private final CoreController coreController;
    public FetchOrder Forders;
    private ProcessOrder POrder;

    public TradeCore(){
        coreController = Main.coreController;
        Forders = new FetchOrder();

        CompletableFuture.runAsync(()-> {
            Main.Exchange.WaitTilAllTickers();
            POrder = new ProcessOrder(Forders);
        });
    }
}
