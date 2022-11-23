package com.coinTradingSystem.CoreController.TradeSystem;

import com.coinTradingSystem.CoreController.CoreController;
import com.coinTradingSystem.Main;
import com.coinTradingSystem.SqlQuery;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Objects;


public class ProcessOrder {
    private CoreController core;
    private FetchOrder orders;
    private String currentExchange;


    private void PlaceSellMarket(String uuid, String symbol, BigDecimal amount) {
        try {
            MarketOrder marketOrder = new MarketOrder(Order.OrderType.ASK, amount, core.exchangeHandler.exchange.AllTickerWithInstrument.get(symbol));
            core.exchangeHandler.exchange.tradeService.placeMarketOrder(marketOrder);
        } catch (Exception e) {
            if (!Main.isExchangeLoaded) return;
            core.controlValue.AddLog("オーダー実行中にエラーが発生しました。");
            core.controlValue.AddLog("オーダーを削除します。");
            core.controlValue.AddLog("オーダー 種類 : 売却 ー 成行注文");
            core.controlValue.AddLog("オーダー UUID : " + uuid);
            core.controlValue.AddLog("オーダー Symbol : " + symbol);
            core.controlValue.AddLog("オーダー 数量 : " + amount);
        }
    }

    private void PlaceBuyMarket(String uuid, String symbol, BigDecimal amount) {
        try {
            MarketOrder marketOrder = new MarketOrder(Order.OrderType.BID, amount, core.exchangeHandler.exchange.AllTickerWithInstrument.get(symbol));
            core.exchangeHandler.exchange.tradeService.placeMarketOrder(marketOrder);
        } catch (Exception e) {
            core.controlValue.AddLog("オーダー実行中にエラーが発生しました。");
            core.controlValue.AddLog("オーダーを削除します。");
            core.controlValue.AddLog("オーダー 種類 : 買収 ー 成行注文");
            core.controlValue.AddLog("オーダー UUID : " + uuid);
            core.controlValue.AddLog("オーダー Symbol : " + symbol);
            core.controlValue.AddLog("オーダー 数量 : " + amount);
        }

    }

    private void PlaceSellLimit(String uuid, String symbol, BigDecimal amount, BigDecimal price) {
        try {
            LimitOrder limitOrder = new LimitOrder(Order.OrderType.ASK, amount, core.exchangeHandler.exchange.AllTickerWithInstrument.get(symbol), null, null, price);
        } catch (Exception e) {
            core.controlValue.AddLog("オーダー実行中にエラーが発生しました。");
            core.controlValue.AddLog("オーダーを削除します。");
            core.controlValue.AddLog("オーダー 種類 : 売却 ー 指値注文");
            core.controlValue.AddLog("オーダー UUID : " + uuid);
            core.controlValue.AddLog("オーダー Symbol : " + symbol);
            core.controlValue.AddLog("オーダー 数量 : " + amount);
            core.controlValue.AddLog("オーダー 価格 : " + price);

        }

    }

    private void PlaceBuyLimit(String uuid, String symbol, BigDecimal amount, BigDecimal price) {
        try {
            LimitOrder limitOrder = new LimitOrder(Order.OrderType.BID, amount, core.exchangeHandler.exchange.AllTickerWithInstrument.get(symbol), null, null, price);
        } catch (Exception e) {
            core.controlValue.AddLog("オーダー実行中にエラーが発生しました。");
            core.controlValue.AddLog("オーダーを削除します。");
            core.controlValue.AddLog("オーダー 種類 : 買収 ー 指値注文");
            core.controlValue.AddLog("オーダー UUID : " + uuid);
            core.controlValue.AddLog("オーダー Symbol : " + symbol);
            core.controlValue.AddLog("オーダー 数量 : " + amount);
            core.controlValue.AddLog("オーダー 価格 : " + price);
        }
    }


    private void BuyOrder(BigDecimal currentPrice, String uuid, String symbol, BigDecimal targetPrice, BigDecimal triggerPrice, BigDecimal amount, boolean MarketPrice) {
        if (currentPrice.compareTo(triggerPrice) > 0) return;
        System.out.println("BUY");
    }

    private void SellOrder(BigDecimal currentPrice, String uuid, String symbol, BigDecimal targetPrice, BigDecimal triggerPrice, BigDecimal amount, boolean MarketPrice) {
        if (currentPrice.compareTo(triggerPrice) < 0) return;
        System.out.println("SELL");
    }

    private void LossCut(BigDecimal currentPrice, String uuid, String symbol, BigDecimal targetPrice, BigDecimal triggerPrice, BigDecimal amount, boolean MarketPrice) {
        if (currentPrice.compareTo(triggerPrice) > 0) return;

        System.out.println("LossCut");
    }

    private void TPCheckTriggerPrice(BigDecimal currentPrice, String uuid, String symbol, BigDecimal triggerPrice, BigDecimal targetPrice, BigDecimal amount) {
        if (currentPrice.compareTo(triggerPrice) >= 0)
            SqlQuery.updateOrder(uuid, Main.CurrentExchange, (short) 4, symbol, BigDecimal.ZERO, targetPrice, amount);

    }

    private void TPCheckTargetPrice(BigDecimal currentPrice, String uuid, String symbol, BigDecimal targetPrice, BigDecimal amount) {
        if (currentPrice.compareTo(targetPrice) <= 0) {
        }
    }

    private void TakeProfit(BigDecimal currentPrice, String uuid, String symbol, BigDecimal targetPrice, BigDecimal triggerPrice, BigDecimal amount, boolean MarketPrice) {
        if (triggerPrice.compareTo(BigDecimal.ZERO) != 0) {
            TPCheckTargetPrice(currentPrice, uuid, symbol, targetPrice, amount);
        } else {
            TPCheckTriggerPrice(currentPrice, uuid, symbol, triggerPrice, targetPrice, amount);
        }

        System.out.println("TakeProfit");
    }

    private BigDecimal cutUnderTheStepSize(String stepSize, BigDecimal amount) {
        BigDecimal tempDecimal = null;
        switch (stepSize) {
            case "10000" -> {
                if (amount.compareTo(new BigDecimal(10000)) >= 0) {
                    tempDecimal = amount.divide(new BigDecimal(10000)).setScale(0, RoundingMode.DOWN).multiply(new BigDecimal(10000));
                }
            }
            case "1000" -> {
                if (amount.compareTo(new BigDecimal(1000)) >= 0) {
                    tempDecimal = amount.divide(new BigDecimal(1000)).setScale(0, RoundingMode.DOWN).multiply(new BigDecimal(1000));
                }
            }
            case "100" -> {
                if (amount.compareTo(new BigDecimal(100)) >= 0) {
                    tempDecimal = amount.divide(new BigDecimal(100)).setScale(0, RoundingMode.DOWN).multiply(new BigDecimal(100));
                }
            }
            case "10" -> {
                if (
                        amount.compareTo(new BigDecimal(10)) >= 0) {
                    tempDecimal = amount.divide(new BigDecimal(10)).setScale(0, RoundingMode.DOWN).multiply(new BigDecimal(10));
                }
            }
            case "1" -> {
                if (amount.compareTo(new BigDecimal(1)) >= 0) {
                    tempDecimal = amount.setScale(0, RoundingMode.UP);
                }
            }
            case "0.1" -> {
                if (amount.compareTo(new BigDecimal("0.1")) >= 0) {
                    tempDecimal = amount.setScale(1, RoundingMode.UP);
                }
            }
            case "0.01" -> {
                if (amount.compareTo(new BigDecimal("0.01")) >= 0) {
                    tempDecimal = amount.setScale(2, RoundingMode.UP);
                }
            }
            case "0.001" -> {
                if (amount.compareTo(new BigDecimal("0.001")) >= 0)  {
                    tempDecimal = amount.setScale(3, RoundingMode.UP);
                }
            }
            case "0.0001" -> {
                if (amount.compareTo(new BigDecimal("0.0001")) >= 0) {
                    tempDecimal = amount.setScale(4, RoundingMode.UP);
                }
            }
            case "0.00001" -> {
                if (amount.compareTo(new BigDecimal("0.00001")) >= 0) {
                    tempDecimal = amount.setScale(5, RoundingMode.UP);
                }
            }
            case "0.000001" -> {
                if (amount.compareTo(new BigDecimal("0.000001")) >= 0) {
                    tempDecimal = amount.setScale(6, RoundingMode.UP);
                }
            }
            case "0.0000001" -> {
                if (amount.compareTo(new BigDecimal("0.0000001")) >= 0) {
                    tempDecimal = amount.setScale(7, RoundingMode.UP);
                }
            }
            case "0.00000001" -> {
                if (amount.compareTo(new BigDecimal("0.00000001")) >= 0) {
                    tempDecimal = amount.setScale(8, RoundingMode.UP);
                }
            }
        }
        return tempDecimal;
    }

    private void OrderProcessPriv() {
        if (orders.orderList.size() == 0) return;
        for (ArrayList<String> item : orders.orderList) {
            String uuid = item.get(0);
            String symbol = item.get(1);
            int orderType = Integer.parseInt(item.get(2));
            BigDecimal targetPrice = new BigDecimal(item.get(3));
            BigDecimal triggerPrice = new BigDecimal(item.get(4));
            String stepSize = core.exchangeHandler.exchange.currencyPairs.getJSONObject(symbol).get("amount_step_size").toString();
            BigDecimal amount = cutUnderTheStepSize(stepSize, new BigDecimal(item.get(5)));
            BigDecimal price = core.exchangeHandler.exchange.getOneTickerPrice(symbol).getLast();
            BigDecimal minimumRequired = core.exchangeHandler.exchange.currencyPairs.getJSONObject(symbol).getBigDecimal("min_amount");
            if (amount == null || amount.multiply(price).compareTo(minimumRequired) < 0) {
                core.controlValue.AddLog("取引の実行に必要な最少額を満たさない為、オーダーがキャンセルされました。");
                core.controlValue.AddLog("オーダー UUID : " + uuid);
                core.controlValue.AddLog("オーダー Symbol : " + symbol);
                core.controlValue.AddLog("オーダー 数量 : " + String.format("%.8f",new BigDecimal(item.get(5))));
                core.controlValue.AddLog("オーダー 価格 : " + price);
                SqlQuery.removeOrder(uuid);
                core.orderTable.InitializeOrderTableView();
            }

            boolean MarketPrice = false;

            if (targetPrice.compareTo(BigDecimal.ZERO) == 0) MarketPrice = true;

            switch (orderType) {
                case 0 -> BuyOrder(price, uuid, symbol, targetPrice, triggerPrice, amount, MarketPrice);
                case 1 -> SellOrder(price, uuid, symbol, targetPrice, triggerPrice, amount, MarketPrice);
                case 2 -> LossCut(price, uuid, symbol, targetPrice, triggerPrice, amount, MarketPrice);
                case 3 -> TakeProfit(price, uuid, symbol, targetPrice, triggerPrice, amount, MarketPrice);
            }


        }
    }


    public void OrderProcessStart() {
        currentExchange = Main.CurrentExchange;
        while (true) {
            if (!Objects.equals(Main.CurrentExchange, currentExchange)) {
                try {
                    Thread.sleep(5000);
                    currentExchange = Main.CurrentExchange;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            OrderProcessPriv();
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }
    }

    public ProcessOrder(CoreController coreController, FetchOrder orders) {
        core = coreController;
        this.orders = orders;
        OrderProcessStart();
    }
}