package com.coinTradingSystem.CoreController.ExchangeHandler;

import com.coinTradingSystem.CoreController.ExchangeHandler.ExchangeClass.Exchanges;
import com.coinTradingSystem.Main;
import com.coinTradingSystem.SqlQuery;
import com.coinTradingSystem.CoreController.CoreController;
import javafx.application.Platform;
import org.knowm.xchange.dto.marketdata.Ticker;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class ExchangeTabFunctions {
    public CoreController coreController;
    public Exchanges exchange;

    private BigDecimal GetTotalBalanceInUSD(){
        BigDecimal totalBalance = new BigDecimal(0);
        for (HashMap<String, String> item : coreController.exchangeHandler.exchange.UserAccountBalance) {
            String symbol = item.get("symbol");
            Ticker ticker = null;
            BigDecimal price = new BigDecimal(1);
            if(!Objects.equals(Main.CurrentExchange, "UPBIT")){
                if (!symbol.contains("USD")) {
                    ticker = exchange.getOneTickerPrice(symbol + "/USDT");
                    price = ticker.getLast();
                }
            }else{
                ticker = exchange.getOneTickerPrice(symbol + "/KRW");
                price = ticker.getLast();
            }
            BigDecimal tempBal = new BigDecimal(item.get("totalbal"));
            totalBalance = totalBalance.add(tempBal.multiply(price));
        }
        return totalBalance;
    }

    public void UpdateStatusTabVariables() {
        coreController.callBackFunctions.WaitTilFetchAPI();
        if (Objects.equals(coreController.exchangeHandler.exchange.isAPINone, "0")) return;
        CompletableFuture.runAsync(() -> {
            try{
                coreController.callBackFunctions.WaitTilExchangeOnlined();
                Platform.runLater(() -> coreController.mainFrame.ExchangeStatus.setText("OK"));
            } catch (Exception e){
                Platform.runLater(() -> coreController.mainFrame.ExchangeStatus.setText("IN MAINTENCE"));
            }
        });
        CompletableFuture.runAsync(() -> coreController.callBackFunctions.WaitTilExchangeTickersUpdated())
                .thenRunAsync(() -> {

                    coreController.exchangeHandler.exchange.getUserAccountBalance();
                })
                .thenRunAsync(()-> {
                    BigDecimal finalTotalBalance = GetTotalBalanceInUSD();
                    Platform.runLater(()->coreController.mainFrame.TotalBalance.setText(String.format("%.3f", finalTotalBalance)));
                });
        CompletableFuture.supplyAsync(()->SqlQuery.getOrderList(Main.CurrentExchange)).thenAcceptAsync((s)->{
            Platform.runLater(()-> coreController.mainFrame.OpenOrders.setText("" + s.size()));
        });
        CompletableFuture.supplyAsync(()->SqlQuery.getTradesNum(Main.CurrentExchange)).thenAcceptAsync((s)->{
            Platform.runLater(()-> coreController.mainFrame.TotalTrades.setText("" + s));
        });
        CompletableFuture.supplyAsync(exchange::getPastWalletInfo).thenAcceptAsync((s)->{
            if(Objects.equals(s,new BigDecimal(-1000)))
                Platform.runLater(()-> coreController.mainFrame.TodayProfit.setText("0"));
            else Platform.runLater(()-> coreController.mainFrame.TodayProfit.setText(String.format("%.8f",s)));

        });
    }

    public void UpdateBalanceTabVariables() {
        if (Objects.equals(coreController.exchangeHandler.exchange.isAPINone, "0")) return;
        CompletableFuture.runAsync(()->coreController.exchangeHandler.exchange.getUserAccountBalance())
                .thenRunAsync(()->Platform.runLater(()->coreController.mainFrame.TotalBalanceInUSD.setText(String.format("%.3f",GetTotalBalanceInUSD()))));
        CompletableFuture.runAsync(()->{
            coreController.balanceTable.InitializeBalanceTableView();
        });
    }

    public void UpdateOrdersTabVariables() {
        if (Objects.equals(coreController.exchangeHandler.exchange.isAPINone, "0")) return;
        CompletableFuture.runAsync(()->coreController.orderTable.InitializeOrderTableView());
    }

}
