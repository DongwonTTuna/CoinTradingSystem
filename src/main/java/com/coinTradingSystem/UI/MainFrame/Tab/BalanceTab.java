package com.coinTradingSystem.UI.MainFrame.Tab;

import com.coinTradingSystem.CoreController.InterFace.FormatString;
import com.coinTradingSystem.CoreController.Exchange.ExchangeMain;
import com.coinTradingSystem.UI.MainFrame.Tab.EventHandler.BalanceTabEventHandler;
import com.coinTradingSystem.Main;
import com.coinTradingSystem.UI.MainFrame.TableVariables;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class BalanceTab extends BalanceTabEventHandler implements FormatString {

    public void InitializeBalanceTableView(ExchangeMain exData) {
        mainFrame.BalanceTable.getItems().clear();
        ObservableList<TableVariables.Balance> balance = mainFrame.BalanceTable.getItems();
        exData.WaitTilUserAccountBalances();

        exData.getUserAccountBalance().forEach(item -> {
            String symbol = item.get("symbol");
            String withdrawable = "Unsupported function for upbit";
            BigDecimal totalamount = new BigDecimal(item.get("totalbal"));
            String amount = String.format("%.8f", totalamount);
            String freeze = String.format("%.8f", new BigDecimal(item.get("freeze")));
            BigDecimal price;
            String currentWorth = null;


            if (Objects.equals(Main.Exchange.getBaseCurrency(),"USDT")){
                price = exData.getOneTicker(symbol + "/USDT").getLast();
                withdrawable = exData.getCurrencies().getJSONObject(symbol).get("wallet_health").toString();
                currentWorth = String.format("%.8f", price.multiply(totalamount));
            }
            else if (Objects.equals(Main.Exchange.getBaseCurrency(), "KRW")) {
                price = exData.getOneTicker(symbol + "/KRW").getLast();
                currentWorth = String.format("%.3f", price.multiply(totalamount));
            } else if (symbol.contains("USD")) {
                price = new BigDecimal(1);
                withdrawable = exData.getCurrencies().getJSONObject(symbol).get("wallet_health").toString();
                currentWorth = String.format("%.8f", price.multiply(totalamount));
            }


            balance.add(new TableVariables.Balance(
                    symbol,
                    getCutStringAmount(amount),
                    getCutStringAmount(freeze),
                    withdrawable,
                    Objects.equals(Main.Exchange.getExchangeName(), "UPBIT") ? "₩ " + getCutStringAmount(currentWorth)  : "$ " + getCutStringAmount(currentWorth)
            ));
        });
        mainFrame.BalanceTable.setItems(balance);

    }



    public void UpdateBalanceTabVariables() {
        CompletableFuture.runAsync(()->Main.Exchange.WaitTilUserAccountBalances())
                .thenRunAsync(()-> Platform.runLater(()->Main.coreController.mainFrame.TotalBalanceInUSD.setText(String.format("%.3f",Main.Exchange.getTotalBalanceInBaseCurrency()))));
        CompletableFuture.runAsync(()->{
            InitializeBalanceTableView(Main.Exchange);
        });
    }

    public BalanceTab() {
        this.mainFrame = Main.mainFrame;
    }
}
