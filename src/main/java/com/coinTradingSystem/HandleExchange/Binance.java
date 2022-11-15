package com.coinTradingSystem.HandleExchange;

import com.coinTradingSystem.Main;
import okhttp3.*;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

class URLs{
    private static final String baseURL = "https://api.binance.com";
    public static final String getAllTickersURL = baseURL +  "/api/v3/exchangeInfo";
    public static final String checkServerURL = baseURL + "/sapi/v1/system/status";
    public static final String serverTimeZoneURL =  baseURL + "/api/v3/time";
    public static final String AssetInfoURL = baseURL + "/sapi/v3/asset/getUserAsset";
    public static final String getOCONumURL = baseURL + "/api/v3/allOrderList";
    public static final String get24TradesNumURL = baseURL + "/api/v3/myTrades";
}



public class Binance {

    public ArrayList<String> allTickers;
    public String apiKey = "";
    private byte[] secretKey;
    private String TZ;

    private void setAPIpriv(){
        HashMap<String,String> API = Main.getOneAPI("BINANCE");
        apiKey = API.get("apikey");
        secretKey = API.get("secretkey").getBytes(StandardCharsets.UTF_8);
    }


    protected String getfromUrl(String url) throws Exception {
        try {
            OkHttpClient client = new OkHttpClient();

            Request.Builder builder = new Request.Builder().url(url).get();
            builder.addHeader("X-MBX-APIKEY", this.apiKey);
            Request request = builder.build();


            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                ResponseBody body = response.body();
                if (body != null) {
                    String str = body.string();
                    //System.out.println(str);
                    return str;
                }
            }
            else
                System.err.println("データの読み込みに失敗しました。");
        } catch(Exception e) {
            throw new RuntimeException();
        }
        return "Error occurred during fetch the data from the url.";
    }
    protected String postfromUrl(String url) throws Exception {
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType MIMEType= MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create (MIMEType,"{}");
            Request request = new Request.Builder().url(url).post(requestBody).addHeader("X-MBX-APIKEY",apiKey).build();
            Response response = client.newCall(request).execute();


            if (response.code() == 200) {
                ResponseBody body = response.body();
                if (body != null) {
                    String str = body.string();
                    //System.out.println(str);
                    return str;
                }
            }
            else
                System.err.println("データの読み込みに失敗しました。");
        } catch(Exception e) {
            throw new RuntimeException();
        }
        return "Error occurred during fetch the data from the url.";
    }
    private String calcSignature(String message){
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "HmacSHA256");
            sha256_HMAC.init(secretKeySpec);
            return "&signature=" + new String(Hex.encodeHex(sha256_HMAC.doFinal(message.getBytes(StandardCharsets.UTF_8))));
        } catch (Exception e) {
            throw new RuntimeException("Unable to sign message.", e);
        }
    }


    private void getServerTimestamp(){
        ParsetoJson tempJson = new ParsetoJson();
        tempJson.run(URLs.serverTimeZoneURL,"get");
        TZ =  "timestamp=" + tempJson.jsonObj.get("serverTime");
    }
    public String isServerOnline(){
        ParsetoJson tempJson = new ParsetoJson();
        tempJson.run(URLs.checkServerURL,"get");
        return "" + tempJson.jsonObj.get("msg");
    }

    public double getAccountBalance(){
        ParsetoJson tempJson = new ParsetoJson();
        getServerTimestamp();
        String parameter = "needBtcValuation=True&" + TZ;
        tempJson.run(URLs.AssetInfoURL + "?" + parameter + calcSignature(parameter),"post");
        double total = 0.0;
        for (Object o : tempJson.jsonArr){
            JSONObject temp = new JSONObject(o.toString());
            total += Double.parseDouble(temp.get("btcValuation").toString());
        }
        return total;
    }

    public int getOCONum(){
        ParsetoJson tempJson = new ParsetoJson();
        getServerTimestamp();
        String parameter = "" + TZ;
        tempJson.run(URLs.getOCONumURL + "?"+ parameter + calcSignature(parameter),"get");
        return tempJson.jsonArr.length();
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
    }*/
    public int getAllTickers(){
        allTickers = new ArrayList<>();
        ParsetoJson tempJson = new ParsetoJson();
        tempJson.run(URLs.getAllTickersURL,"get");
        JSONArray json = new JSONArray(tempJson.jsonObj.get("symbols").toString());
        json.forEach((i)->{
            String a = new JSONObject(i.toString()).get("symbol").toString();
            if (a.contains("USDT")){
                allTickers.add(a);
            }
        });
        return 0;
    }
    public Binance(){
        setAPIpriv();
    }
}
class ParsetoJson extends Binance{
    public JSONObject jsonObj;
    public JSONArray jsonArr;

    public void run(String url,String toggle){
            String data = "";
            try {
                if (Objects.equals(toggle, "get")) data = getfromUrl(url);
                else data = postfromUrl(url);
                jsonObj = new JSONObject(data);
            }catch (JSONException e){
                jsonArr = new JSONArray(data);
            } catch (Exception e){
                throw new RuntimeException(e);
            }
    }
}