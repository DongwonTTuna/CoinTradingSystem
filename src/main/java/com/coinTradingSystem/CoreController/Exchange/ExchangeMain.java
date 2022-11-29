package com.coinTradingSystem.CoreController.Exchange;

import com.coinTradingSystem.CoreController.Exchange.ExchangeHandler.Exchanges;
import com.coinTradingSystem.CoreController.InterFace.FormatString;
import com.coinTradingSystem.SqlQuery;
import org.json.JSONObject;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.instrument.Instrument;
import org.knowm.xchange.service.trade.TradeService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static org.awaitility.Awaitility.await;

public class ExchangeMain implements FormatString {
    private HashMap<String, Instrument> AllTickerWithInstrument;
    private ArrayList<String> AllTickers;
    private final Exchanges exchange;
    private JSONObject currencyPairs;
    private JSONObject currencies;
    private List<HashMap<String,String>> UserAccountBalance;
    private BigDecimal TotalBalanceInBaseCurrency;
    private BigDecimal PastWalletValue;
    private String BaseCurrency;
    public HashMap<String,String> TextHash = new HashMap<>();

    public String getExchangeName(){
        return exchange.ExchangeName;
    }
    public String getBaseCurrency() { return BaseCurrency;}
    public Ticker getOneTicker(String symbol){
        return exchange.getOneTickerPrice(symbol);
    }
    public void updateYesterDayWalletValue(BigDecimal value){
        this.PastWalletValue = value;
    }
    public void updateTotalBalanceInBC(BigDecimal value){
        this.TotalBalanceInBaseCurrency = value;
    }
    public void updateUserAccountBalance(List<HashMap<String,String>> UserAccountBalance){
        this.UserAccountBalance = UserAccountBalance;
    }
    public void updateBaseCurrency(String baseCurrency){
        this.BaseCurrency = baseCurrency;
    }
    public void updateCurrencies (JSONObject currencies){
        this.currencies = currencies;
    }

    public void updateCurrencyPairs (JSONObject currencyPairs){
        this.currencyPairs = currencyPairs;
    }
    public void updateAllTickers(ArrayList<String> allTickers){
        this.AllTickers = allTickers;
    }

    public void updateAllTWithI(HashMap<String,Instrument> allTickerWithInstrument){
        this.AllTickerWithInstrument = allTickerWithInstrument;
    }
    public BigDecimal getTotalBalanceInBaseCurrency(){
        return this.TotalBalanceInBaseCurrency;
    }

    public BigDecimal getYesterDayWalletValue(){
        return this.PastWalletValue;
    }
    public HashMap<String, Instrument> getAllTickerWithInstrument(){
        return this.AllTickerWithInstrument;
    }
    public ArrayList<String> getAllTickers(){
        return this.AllTickers;
    }

    public TradeService getTradeService(){
        return exchange.tradeService;
    }

    public JSONObject getCurrencyPairs(){
        return this.currencyPairs;
    }
    public JSONObject getCurrencies(){
        return this.currencies;
    }
    public String getAPIStatus(){
        return exchange.isAPINone;
    }
    public List<HashMap<String,String>> getUserAccountBalance(){
        return this.UserAccountBalance;
    }
    public void WaitTilAllTickers(){
        await().until(()->this.AllTickers != null);
    }
    public void WaitTilAllTWithI(){
        await().until(()->this.AllTickerWithInstrument != null);
    }
    public void WaitTilExchange(){
        await().until(()->this.exchange != null);
    }
    public void WaitTilcurrencyPairs(){
        await().until(()->this.currencyPairs != null);
    }
    public void WaitTilcurrencies(){
        await().until(()->this.currencies != null);
    }
    public void WaitTilUserAccountBalances(){
        await().until(()->this.UserAccountBalance != null);
    }
    public void WaitTilTotalBalanceInBC(){
        await().until(()->this.TotalBalanceInBaseCurrency != null);
    }
    public void WaitTilApiLoad(){
     await().until(()->this.exchange.isAPINone != null);
    }
    public void WaitTilExchangeName(){await().until(()->exchange.ExchangeName != null);}
    public ExchangeMain(String ExchangeClassName){
        exchange = new Exchanges(ExchangeClassName,this);
        UpdateHashmap();
    }
    public void UpdateHashmap(){
        WaitTilExchangeName();
        CompletableFuture.runAsync(()->{
            while (true){
                CompletableFuture.runAsync(()->{
                    try{
                        WaitTilExchange();
                        TextHash.put("ExchangeStatus","OK");
                    }catch (Exception e){
                        TextHash.put("ExchangeStatus","IN MAINTENCE");
                    }
                });
                TextHash.put("TotalBalanceInBC",String.format("%.3f",getTotalBalanceInBaseCurrency()));
                TextHash.put("TradesNum", "" + SqlQuery.getTradesNum(getExchangeName()));
                TextHash.put("OrdersNum", "" + SqlQuery.getOrderList(getExchangeName()));
                if(getYesterDayWalletValue() != null) TextHash.put("YesterDayWalletValue",Objects.equals(getYesterDayWalletValue(),new BigDecimal(-1000)) ? "0" : getYesterDayWalletValue().toString());
                try{
                    Thread.sleep(5000);
                } catch (Exception ignored)
                {           }
            }
        });
    }
}
