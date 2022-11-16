
package com.coinTradingSystem.HandleExchange.Binance;

import com.coinTradingSystem.HandleExchange.URLs;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;





public class Binance extends BinanceFunction{

    public Callable<Boolean> DoesInitializeDone(){
        return () -> super.allTickersPrice != null;
    }
    public void InitializeHash(){
        CompletableFuture.runAsync(super::getAllTickers).thenRunAsync(()->{
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("[");
            super.allTickers.forEach((item)->{
                strBuilder.append("\"").append(item).append("\",");
            });
            strBuilder.replace(strBuilder.length()-1,strBuilder.length(),"");
            strBuilder.append("]");
            ParameterHash.put("AllSymbols","symbols=" + strBuilder.toString());
        }).thenRunAsync(super::getAllSymbolPrice);
        ParameterHash.put("WalletType","type=SPOT");
        ParameterHash.put("needBtcValuation","needBtcValuation=True");


        isNeedSignature.put(URLs.binance.AssetInfoURL,true);
        isNeedSignature.put(URLs.binance.checkServerURL,false);
        isNeedSignature.put(URLs.binance.getAllCoinInfosURL,true);
        isNeedSignature.put(URLs.binance.getOCONumURL,true);
        isNeedSignature.put(URLs.binance.getPBalURL,true);
        isNeedSignature.put(URLs.binance.serverTimeZoneURL,false);
        isNeedSignature.put(URLs.binance.getAllTickersURL,false);
        isNeedSignature.put(URLs.binance.getSymbolPriceURL,false);
    }

    public Binance(){
        super.setAPIpriv();
    }
}