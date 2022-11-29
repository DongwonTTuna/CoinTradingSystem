package com.coinTradingSystem.CoreController;

import com.coinTradingSystem.CoreController.Exchange.OrderThread.TradeCore;
import com.coinTradingSystem.CoreController.Exchange.ExchangeMain;
import com.coinTradingSystem.Main;
import com.coinTradingSystem.UI.MainFrame.MainFrame;
import org.knowm.xchange.binance.BinanceExchange;
import org.knowm.xchange.gateio.GateioExchange;
import org.knowm.xchange.upbit.UpbitExchange;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;


public class CoreController {


    public MainFrame mainFrame;
    public TradeCore tradeCore;
    public HashMap<String, ExchangeMain> AllExchanges;

    public void addExchanges(){
        HashMap<String, ExchangeMain> tempHash = new HashMap<>();
        tempHash.put("BINANCE",new ExchangeMain(BinanceExchange.class.getName()));
        tempHash.put("UPBIT",new ExchangeMain(UpbitExchange.class.getName()));
        tempHash.put("GATEIO",new ExchangeMain(GateioExchange.class.getName()));
        AllExchanges = tempHash;
    }

    public CoreController(){
        Main.coreController = this;
        this.mainFrame = Main.mainFrame;
        CompletableFuture.runAsync(this::addExchanges);
        this.tradeCore = new TradeCore();
    }
}
