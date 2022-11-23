package com.coinTradingSystem.CoreController.Tables;

import com.coinTradingSystem.CoreController.OrderTabEvent;
import com.coinTradingSystem.Main;
import com.coinTradingSystem.CoreController.CoreController;
import com.coinTradingSystem.UI.MainFrame.TableVariables;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.util.Objects;

public class BalanceTable implements OrderTabEvent {
    private final CoreController coreController;

    public void InitializeBalanceTableView() {
        coreController.mainFrame.BalanceTable.getItems().clear();
        ObservableList<TableVariables.Balance> balance = coreController.mainFrame.BalanceTable.getItems();
        coreController.callBackFunctions.WaitTilAccountBalanceInitialized();
        coreController.exchangeHandler.exchange.UserAccountBalance.forEach(item -> {
            String symbol = item.get("symbol");
            String withdrawable = "Unsupported function for upbit";
            BigDecimal totalamount = new BigDecimal(item.get("totalbal"));
            String amount = String.format("%.8f", totalamount);
            String freeze = String.format("%.8f", new BigDecimal(item.get("freeze")));
            BigDecimal price;
            String currentWorth;
            if (Objects.equals(Main.CurrentExchange, "UPBIT")) {
                price = coreController.exchangeHandler.exchange.getOneTickerPrice(symbol + "/KRW").getLast();
                currentWorth = String.format("%.3f", price.multiply(totalamount));
            } else if (symbol.contains("USD")) {
                price = new BigDecimal(1);
                withdrawable = coreController.exchangeHandler.exchange.currencies.getJSONObject(symbol).get("wallet_health").toString();
                currentWorth = String.format("%.8f", price.multiply(totalamount));
            } else {
                price = coreController.exchangeHandler.exchange.getOneTickerPrice(symbol + "/USDT").getLast();
                withdrawable = coreController.exchangeHandler.exchange.currencies.getJSONObject(symbol).get("wallet_health").toString();
                currentWorth = String.format("%.8f", price.multiply(totalamount));
            }


            balance.add(new TableVariables.Balance(
                    symbol,
                    getCutStringAmount(amount),
                    getCutStringAmount(freeze),
                    withdrawable,
                    Objects.equals(Main.CurrentExchange, "UPBIT") ? "₩ " + getCutStringAmount(currentWorth)  : "$ " + getCutStringAmount(currentWorth)
            ));
        });
        coreController.mainFrame.BalanceTable.setItems(balance);

    }

    private void setCellValue() {
        coreController.mainFrame.BalanceSymbol.setCellValueFactory(new PropertyValueFactory<TableVariables.Balance, String>("symbol"));
        coreController.mainFrame.BalanceAmount.setCellValueFactory(new PropertyValueFactory<TableVariables.Balance, String>("amount"));
        coreController.mainFrame.BalanceFreeze.setCellValueFactory(new PropertyValueFactory<TableVariables.Balance, String>("freeze"));
        coreController.mainFrame.BalanceWithdrawable.setCellValueFactory(new PropertyValueFactory<TableVariables.Balance, String>("withdrawable"));
        coreController.mainFrame.BalanceWorthInUSD.setCellValueFactory(new PropertyValueFactory<TableVariables.Balance, String>("worthofusd"));
    }

    public BalanceTable(CoreController coreController) {
        this.coreController = coreController;
        setCellValue();
    }
}
