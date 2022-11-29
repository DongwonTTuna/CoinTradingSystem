package com.coinTradingSystem.UI.MainFrame.Tab;

import com.coinTradingSystem.UI.MainFrame.MainFrame;
import com.coinTradingSystem.UI.MainFrame.Tab.EventHandler.StatusTabEventHandler;
import com.coinTradingSystem.Main;
import javafx.application.Platform;

import java.math.BigDecimal;
import java.util.Objects;

public class StatusTab extends StatusTabEventHandler {
    public void UpdateStatusTabVariables() {
        Platform.runLater(()->mainFrame.ExchangeStatus.setText(Main.Exchange.TextHash==null ?"Fetching..." : Main.Exchange.TextHash.get("ExchangeStatus") ));
        Platform.runLater(()->mainFrame.TotalBalance.setText(Main.Exchange.TextHash==null ? "Fetching...":Main.Exchange.TextHash.get("TotalBalanceInBC")));
        Platform.runLater(()->mainFrame.TotalTrades.setText(Main.Exchange.TextHash==null ? "Fetching...":Main.Exchange.TextHash.get("TradesNum")));
        Platform.runLater(()->mainFrame.OpenOrders.setText(Main.Exchange.TextHash==null ? "Fetching...":Main.Exchange.TextHash.get("OrdersNum")));
        if (!Objects.equals(Main.Exchange.TextHash.get("TotalBalanceInBC"), "nul"))
            Platform.runLater(()->mainFrame.TodayProfit.setText(Main.Exchange.getCutStringPrice(
                    new BigDecimal(Main.Exchange.TextHash.get("TotalBalanceInBC")).subtract(new BigDecimal(Main.Exchange.TextHash.get("YesterDayWalletValue"))).toString())));
    }

    public StatusTab(){
        this.mainFrame = Main.mainFrame;
    }

}
