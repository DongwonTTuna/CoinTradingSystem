package com.coinTradingSystem.UI.MainFrame;

import com.coinTradingSystem.UI.MainFrame.CoreController.Tables.BalanceTable;
import com.coinTradingSystem.UI.MainFrame.CoreController.Tables.OrderTable;
import com.coinTradingSystem.UI.MainFrame.Variables;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TableVariables {
    public static class Order {
        private final String uuid;
        private final String symbol;
        private final String ordertype;
        private final String targetprice;
        private final String triggerprice;
        private final String amount;


        public Order(String uuid, String symbol, String ordertype, String targetprice, String triggerprice, String amount) {
            this.uuid = uuid;
            this.symbol = symbol;
            this.ordertype = ordertype;
            this.targetprice = targetprice;
            this.triggerprice = triggerprice;
            this.amount = amount;
        }

        public String getUuid() {
            return this.uuid;
        }

        public String getSymbol() {
            return this.symbol;
        }

        public String getOrdertype() {
            return this.ordertype;
        }

        public String getTargetprice() {
            return this.targetprice;
        }

        public String getTriggerprice() {
            return this.triggerprice;
        }

        public String getAmount() {
            return this.amount;
        }


    }

    public static class Balance {
        private final String symbol;
        private final String amount;
        private final String freeze;
        private final String withdrawable;
        private final String worthofusd;

        public Balance(String symbol, String amount, String freeze, String withdrawable, String worthofusd) {
            this.symbol = symbol;
            this.amount = amount;
            this.freeze = freeze;
            this.withdrawable = withdrawable;
            this.worthofusd = worthofusd;
        }

        public String getSymbol() {
            return symbol;
        }

        public String getAmount() {
            return amount;
        }

        public String getFreeze() {
            return freeze;
        }

        public String getWithdrawable() {
            return withdrawable;
        }

        public String getWorthofusd() {
            return worthofusd;
        }
    }

    @FXML
    public TableView<Order> OrderTable;
    @FXML
    public TableColumn<Order, String> OrderUUID;
    @FXML
    public TableColumn<Order, String> OrderSymbol;
    @FXML
    public TableColumn<Order, String> OrderOrderType;
    @FXML
    public TableColumn<Order, String> OrderTargetPrice;
    @FXML
    public TableColumn<Order, String> OrderTriggerPrice;
    @FXML
    public TableColumn<Order, String> OrderAmount;

    @FXML
    public TableView<Balance> BalanceTable;
    @FXML
    public TableColumn<Balance, String> BalanceSymbol;
    @FXML
    public TableColumn<Balance, String> BalanceAmount;
    @FXML
    public TableColumn<Balance, String> BalanceFreeze;
    @FXML
    public TableColumn<Balance, String> BalanceWithdrawable;
    @FXML
    public TableColumn<Balance, String> BalanceWorthInUSD;
}
