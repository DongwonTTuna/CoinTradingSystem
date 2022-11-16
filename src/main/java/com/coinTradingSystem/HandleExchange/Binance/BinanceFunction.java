package com.coinTradingSystem.HandleExchange.Binance;

import com.coinTradingSystem.HandleExchange.HandleHTML;
import com.coinTradingSystem.HandleExchange.URLs;
import com.coinTradingSystem.Main;
import com.coinTradingSystem.SqlQuery;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class BinanceFunction extends HandleHTML {
        public ArrayList<String> allTickers;
        public HashMap<String,Double> allTickersPrice;

        public void setAPIpriv(){
            HashMap<String,String> API = Main.getOneAPI("BINANCE");
            apiKey = API.get("apikey");
            secretKey = API.get("secretkey").getBytes(StandardCharsets.UTF_8);
        }


        public String isServerOnline(){
            JSONObject tempJson = (JSONObject) super.run(URLs.binance.checkServerURL,null,"get");
            return "" + tempJson.get("msg");
        }

        public double getAccountBalance(){
            JSONArray TempJson = (JSONArray) super.run(URLs.binance.AssetInfoURL, new String[]{"needBtcValuation", "TZ"},"post");
            double total = 0.0;
            for (Object o : TempJson){
                JSONObject temp = new JSONObject(o.toString());
                total += Double.parseDouble(temp.get("btcValuation").toString());
            }
            return total;
        }


        public int getOCONum(){
            return ((JSONArray)super.run(URLs.binance.getOCONumURL,new String[]{"TZ"},"get")).length();
        }

    /*
    * Deprecated
    *
    public void get24TradesNum(){
        HashMap<String, Integer> result = new HashMap<>();
        for (String symbol : allTickers){
            CompletableFuture.runAsync(()->{
                ParsetoJson tempJson = new ParsetoJson();
                getServerTimestamp();
                String parameter = "symbol=" + symbol + "&endTime="+System.currentTimeMillis() +"&startTime="+ (System.currentTimeMillis() - 3600*24)+ "&" + TZ;
                tempJson.run(URLs.get24TradesNumURL + "?" + parameter + calcSignature(parameter),"get");
                result.put(symbol, tempJson.jsonArr.length());
            });
        }
        while(result.size() == allTickers.size()){
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            }
        System.out.println(result);
        //temp
    }
    *
    public double getCoinBalance(){
            JSONArray TempJson = (JSONArray) super.run(URLs.binance.AssetInfoURL, new String[]{"needBtcValuation", "TZ"},"post");
            double total = 0.0;
            for (Object o : TempJson){
                JSONObject temp = new JSONObject(o.toString());
                total += Double.parseDouble(temp.get("btcValuation").toString());
            }
            return total;
        }
    *
    */

        public void getAllTickers(){
            ArrayList<String> temp = new ArrayList<>();
            JSONArray json = new JSONArray(((JSONObject)super.run(URLs.binance.getAllTickersURL,null,"get")).get("symbols").toString());
            json.forEach((i)->{
                String a = new JSONObject(i.toString()).get("symbol").toString();
                if (a.contains("USDT") && !a.contains("UP") && !a.contains("DOWN") && !a.contains("BULL") && !a.contains("BEAR")){
                    temp.add(a);
                }
            });
            this.allTickers = temp;
        }

        public double getPbal(){
            JSONArray obj = ((JSONObject) super.run(URLs.binance.getPBalURL,new String[]{"WalletType","TZ"},"get")).getJSONArray("snapshotVos");
            int num = obj.length();
            JSONObject tempO = obj.getJSONObject(num-1).getJSONObject("data");
            SqlQuery.updatePBal("BINANCE", tempO.getDouble("totalAssetOfBtc"));
            return tempO.getDouble("totalAssetOfBtc");
        }

        public ArrayList<HashMap<String,String>> getAllcoinInfo(){
            ArrayList<HashMap<String,String>> result =  new ArrayList<>();

            for (Object o : (JSONArray) super.run(URLs.binance.getAllCoinInfosURL,new String[]{"TZ"},"get")){
                HashMap<String, String> tempHash = new HashMap<>();
                JSONObject tempObj = new JSONObject(o.toString());
                if (tempObj.getDouble("free") == 0.0 && tempObj.getDouble("freeze") == 0.0) continue;
                tempHash.put("symbol", tempObj.getString("coin"));
                tempHash.put("amount", tempObj.get("free").toString());
                tempHash.put("freeze", tempObj.get("freeze").toString());
                tempHash.put("withdrawable", tempObj.get("withdrawAllEnable").toString());
                result.add(tempHash);
            }
            return result;
        }

        public void getAllSymbolPrice(){
            JSONArray tempJson = (JSONArray) super.run(URLs.binance.getSymbolPriceURL,new String[]{"AllSymbols"},"get");
            HashMap<String,Double> tempTickerPrice = new HashMap<>();
            tempJson.forEach((item)->{
                JSONObject tempItem = (JSONObject) item;
                tempTickerPrice.put(tempItem.getString("symbol"),tempItem.getDouble("price"));
            });
            this.allTickersPrice = tempTickerPrice;
        }
}
