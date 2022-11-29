package com.coinTradingSystem.CoreController.Exchange.ExchangeHandler;

import com.coinTradingSystem.CoreController.Exchange.ExchangeMain;
import com.coinTradingSystem.SqlQuery;
import org.json.JSONObject;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.trade.TradeService;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.awaitility.Awaitility.await;

public class ExchangeFunctions {
    public String ExchangeName;
    public String isAPINone;
    public Exchange exchange;
    public AccountService accountService;
    public MarketDataService marketDataService;
    public TradeService tradeService;
    public ExchangeMain exData;

    private void WaitTilAccountService(){
        await().until(()->accountService != null);
    }
    private void WaitTilMarketDataService(){
        await().until(()->marketDataService != null);
    }
    private void WaitTilTradeService(){
        await().until(()->tradeService != null);
    }


    public void runAll(){
        WaitTilAccountService();
        WaitTilMarketDataService();
        WaitTilTradeService();

        CompletableFuture.runAsync(this::getUserAccountBalance);
        CompletableFuture.runAsync(this::getAllTickers).thenRunAsync(()->{
            exData.WaitTilUserAccountBalances();
            GetTotalBalanceInUSD();
            getWalletInfo();
            getPastWalletInfo();
        });
    }

    private void getAllTickers() {
        HashMap<String,Instrument> tempHash = new HashMap<>();
        ArrayList<String> tempArr = new ArrayList<>();
        if (Objects.equals(ExchangeName, "UPBIT")){
            exchange.getExchangeInstruments().forEach((item)->{
                if (item.toString().matches("[a-zA-Z0-9]+/KRW")){
                    tempHash.put(item.toString(),item);
                    tempArr.add(item.toString());
                }
            });
        }else{
            exchange.getExchangeInstruments().forEach((item) -> {
                if ((item.toString().
                        matches("[a-zA-Z0-9]+/USDT") &&
                        !item.toString().contains("UP")) &&
                        !item.toString().contains("DOWN")) {
                    tempHash.put(item.toString(),item);
                    tempArr.add(item.toString());
                }
            });
        }

        tempArr.sort(String.CASE_INSENSITIVE_ORDER);

        exData.updateAllTWithI(tempHash);
        exData.updateAllTickers(tempArr);
    }


    public Ticker getOneTickerPrice(String symbol){
        try {
            return marketDataService.getTicker((CurrencyPair) exData.getAllTickerWithInstrument().get(symbol));
        } catch (Exception ignored) {
            return null;
        }
    }

    private void GetTotalBalanceInUSD(){
        BigDecimal totalBalance = new BigDecimal(0);
        for (HashMap<String, String> item : exData.getUserAccountBalance()) {
            String symbol = item.get("symbol");
            Ticker ticker;
            BigDecimal price = new BigDecimal(1);
            if(!Objects.equals(ExchangeName, "UPBIT")){
                if (!symbol.contains("USD")) {
                    ticker = getOneTickerPrice(symbol + "/USDT");
                    price = ticker.getLast();
                }
            }else{
                ticker = getOneTickerPrice(symbol + "/KRW");
                price = ticker.getLast();
            }
            BigDecimal tempBal = new BigDecimal(item.get("totalbal"));
            totalBalance = totalBalance.add(tempBal.multiply(price));
        }
        exData.updateTotalBalanceInBC(totalBalance);
    }
    private void getUserAccountBalance() {
        try {
            ArrayList<Balance> arr = new ArrayList<>(accountService.getAccountInfo().getWallet().balances());
            ArrayList<HashMap<String,String>> tempArrHashmap = new ArrayList<>();
            arr.forEach((item)->{
                BigDecimal totalbal = item.getTotal();
                if (!(totalbal.compareTo(BigDecimal.ZERO) > 0)) return;
                String symbol = item.getCurrency().toString();

                if(Objects.equals(ExchangeName, "UPBIT")){
                    if(!exData.getAllTickers().contains(symbol + "/KRW")) return;
                }else{
                    if (!Objects.equals(symbol, "USDT") && !exData.getAllTickers().contains(symbol + "/USDT")) return;
                }

                HashMap<String,String> tempHashmap = new HashMap<>();
                tempHashmap.put("symbol", symbol);
                tempHashmap.put("totalbal", totalbal.toEngineeringString());
                tempHashmap.put("free",item.getAvailable().toString());
                tempHashmap.put("freeze",item.getFrozen().toString());
                tempHashmap.put("withdrawable",item.getAvailableForWithdrawal().toString());
                tempArrHashmap.add(tempHashmap);
            });
            exData.updateUserAccountBalance(tempArrHashmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getWalletInfo(){
        try{
             JSONObject tempJson = new JSONObject(exchange.getExchangeMetaData().toJSONString());
             try{
                 exData.updateCurrencyPairs(tempJson.getJSONObject("currency_pairs"));
                 exData.updateCurrencies(tempJson.getJSONObject("currencies"));
             } catch (Exception ignored){
             }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void getPastWalletInfo(){
        exData.updateYesterDayWalletValue(SqlQuery.getLastProfit(ExchangeName));
    }
}
