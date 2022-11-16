package com.coinTradingSystem.HandleExchange;

public class URLs {
    public static class binance{
        private static final String baseURL = "https://api.binance.com";
        public static final String getAllTickersURL = baseURL +  "/api/v3/exchangeInfo";
        public static final String checkServerURL = baseURL + "/sapi/v1/system/status";
        public static final String serverTimeZoneURL =  baseURL + "/api/v3/time";
        public static final String AssetInfoURL = baseURL + "/sapi/v3/asset/getUserAsset";
        public static final String getOCONumURL = baseURL + "/api/v3/allOrderList";
        public static final String getPBalURL = baseURL + "/sapi/v1/accountSnapshot";
        public static final String getAllCoinInfosURL = baseURL + "/sapi/v1/capital/config/getall";
        public static final String getSymbolPriceURL = baseURL + "/api/v3/ticker/price";
    }

    public static class gateio{
        private static final String baseURL = "";
    }
}
