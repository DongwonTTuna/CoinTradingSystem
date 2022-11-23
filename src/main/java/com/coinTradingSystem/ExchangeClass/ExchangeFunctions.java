package com.coinTradingSystem.ExchangeClass;

import com.coinTradingSystem.Main;
import com.coinTradingSystem.SqlQuery;
import com.coinTradingSystem.CoreController.CoreController;
import org.json.JSONObject;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.account.Balance;
import org.knowm.xchange.dto.account.Wallet;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.service.trade.TradeService;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ExchangeFunctions {
    public String isAPINone;
    public CoreController core;
    public Exchange exchange;
    public AccountService accountService;
    public MarketDataService marketDataService;
    public TradeService tradeService;
    public HashMap<String,Instrument> AllTickerWithInstrument;
    public ArrayList<String> AllTickers;

    public JSONObject currencyPairs;
    public JSONObject currencies;
    public List<HashMap<String,String>> UserAccountBalance;
    public boolean initializeDone;

    public Wallet getWalletBalance() {
        try {
            return accountService.getAccountInfo().getWallet();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }


    private void getAllTickers() {
        HashMap<String,Instrument> tempHash = new HashMap<>();
        ArrayList<String> tempArr = new ArrayList<>();
        if (Objects.equals(Main.CurrentExchange, "UPBIT")){
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

        AllTickerWithInstrument = tempHash;
        AllTickers = tempArr;
    }

    /*
    public void getAllPrices() {
        ArrayList<Ticker> tempList = new ArrayList<>();
        AllTicker.forEach((item) -> {
                CompletableFuture.runAsync(()-> {
                    try {
                        tempList.add(marketDataService.getTicker((CurrencyPair) item));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        });
        AllTickerPriceData = tempList;
    }
     */

    public Ticker getOneTickerPrice(String symbol){
            try {
                return marketDataService.getTicker((CurrencyPair) AllTickerWithInstrument.get(symbol));
            }catch (Exception ignored){
                return null;
            }
    }


    public void InitializeTickersAndPrice() {
        CompletableFuture.runAsync(this::getAllTickers);
        CompletableFuture.runAsync(this::getUserAccountBalance);
    }

    public void getUserAccountBalance() {
        try {
            core.callBackFunctions.WaitTilAccountIsNotNull();
            ArrayList<Balance> arr = new ArrayList<>(accountService.getAccountInfo().getWallet().balances());
            ArrayList<HashMap<String,String>> tempArrHashmap = new ArrayList<>();
            arr.forEach((item)->{
                BigDecimal totalbal = item.getTotal();
                if (!(totalbal.compareTo(BigDecimal.ZERO) > 0)) return;
                String symbol = item.getCurrency().toString();

                if(Objects.equals(Main.CurrentExchange, "UPBIT")){
                    if(!AllTickers.contains(symbol + "/KRW")) return;
                }else{
                    if (!Objects.equals(symbol, "USDT") && !AllTickers.contains(symbol + "/USDT")) return;
                }

                HashMap<String,String> tempHashmap = new HashMap<>();
                tempHashmap.put("symbol", symbol);
                tempHashmap.put("totalbal", totalbal.toEngineeringString());
                tempHashmap.put("free",item.getAvailable().toString());
                tempHashmap.put("freeze",item.getFrozen().toString());
                tempHashmap.put("withdrawable",item.getAvailableForWithdrawal().toString());
                tempArrHashmap.add(tempHashmap);
            });
            UserAccountBalance = tempArrHashmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getWalletInfo(){
        try{
             JSONObject tempJson = new JSONObject(exchange.getExchangeMetaData().toJSONString());
             try{
                 currencyPairs = tempJson.getJSONObject("currency_pairs");
                 currencies = tempJson.getJSONObject("currencies");
             } catch (Exception ignored){

             }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public BigDecimal getPastWalletInfo(){
        return SqlQuery.getLastProfit(Main.CurrentExchange);
    }
}
