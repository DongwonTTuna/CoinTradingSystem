package com.coinTradingSystem.HandleExchange;

import okhttp3.*;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Objects;

public abstract class HandleHTML{

    public String apiKey = "";
    public byte[] secretKey;
    public HashMap<String,String> ParameterHash = new HashMap<>();

    public HashMap<String,Boolean> isNeedSignature = new HashMap<>();



    public abstract void InitializeHash();


    protected String calcSignature(String message){
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "HmacSHA256");
            sha256_HMAC.init(secretKeySpec);
            return "signature=" + new String(Hex.encodeHex(sha256_HMAC.doFinal(message.getBytes(StandardCharsets.UTF_8))));
        } catch (Exception e) {
            throw new RuntimeException("Unable to sign message.", e);
        }
    }

    protected String getServerTimestamp() {
        try {
            JSONObject jsonObj = new JSONObject(getfromUrl(URLs.binance.serverTimeZoneURL));
            return "timestamp=" + jsonObj.get("serverTime");
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    protected Object run(String URL,String[] parameter, String toggle){
        String parameterString = "";
        if (parameter != null){
            StringBuilder parameterBuilder = new StringBuilder();
            String TZ = "";

            for(String param: parameter){
                switch(param){
                    case "TZ" -> {TZ = getServerTimestamp(); parameterBuilder.append(TZ);}
                    case "WalletType" -> parameterBuilder.append(ParameterHash.get("WalletType"));
                    case "needBtcValuation" -> parameterBuilder.append(ParameterHash.get("needBtcValuation"));
                    case "AllSymbols" -> parameterBuilder.append(ParameterHash.get("AllSymbols"));
                }
                parameterBuilder.append("&");
            }
            parameterBuilder.replace(parameterBuilder.length()-1,parameterBuilder.length(),"");
            parameterString = parameterBuilder.toString();
        }

        if(isNeedSignature.get(URL)) parameterString += "&" + calcSignature(parameterString);
        String data = "";
        try {
            if (Objects.equals(toggle, "get")) data = getfromUrl(URL + "?" + parameterString);
            else data = postfromUrl(URL + "?" + parameterString);

            return new JSONObject(data);
        }catch (JSONException e){
            return new JSONArray(data);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    protected String getfromUrl(String url){
        try {
            OkHttpClient client = new OkHttpClient();
            Request.Builder builder = new Request.Builder().url(url).get();
            builder.addHeader("X-MBX-APIKEY", apiKey);
            Request request = builder.build();


            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                ResponseBody body = response.body();
                if (body != null) {
                    return body.string();
                }
            }
            else
                System.err.println("データの読み込みに失敗しました。");
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        return "Error occurred during fetch the data from the url.";
    }
    protected String postfromUrl(String url){
        try {
            OkHttpClient client = new OkHttpClient();
            MediaType MIMEType= MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create (MIMEType,"{}");
            Request request = new Request.Builder().url(url).post(requestBody).addHeader("X-MBX-APIKEY",apiKey).build();
            Response response = client.newCall(request).execute();


            if (response.code() == 200) {
                ResponseBody body = response.body();
                if (body != null) {
                    return body.string();
                }
            }
            else
                System.err.println("データの読み込みに失敗しました。");
        } catch(Exception e) {
            throw new RuntimeException();
        }
        return "Error occurred during fetch the data from the url.";
    }
    public HandleHTML(){
        InitializeHash();
    }
}