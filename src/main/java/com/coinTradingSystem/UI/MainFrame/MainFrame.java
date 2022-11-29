package com.coinTradingSystem.UI.MainFrame;

import com.coinTradingSystem.CoreController.CoreController;
import com.coinTradingSystem.CoreController.InterFace.AddLog;
import com.coinTradingSystem.UI.MainFrame.EventHandler.EventHandler;
import com.coinTradingSystem.UI.MainFrame.Tab.UpdateTab;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;


public class MainFrame extends EventHandler {

    private void setCellValue() {
        OrderUUID.setCellValueFactory(new PropertyValueFactory<Order, String>("uuid"));
        OrderSymbol.setCellValueFactory(new PropertyValueFactory<TableVariables.Order, String>("symbol"));
        OrderOrderType.setCellValueFactory(new PropertyValueFactory<TableVariables.Order, String>("ordertype"));
        OrderTargetPrice.setCellValueFactory(new PropertyValueFactory<TableVariables.Order, String>("targetprice"));
        OrderTriggerPrice.setCellValueFactory(new PropertyValueFactory<TableVariables.Order, String>("triggerprice"));
        OrderAmount.setCellValueFactory(new PropertyValueFactory<TableVariables.Order, String>("amount"));
        OrderWorthInUsd.setCellValueFactory(new PropertyValueFactory<TableVariables.Order, String>("usdworth"));

        BalanceSymbol.setCellValueFactory(new PropertyValueFactory<TableVariables.Balance, String>("symbol"));
        BalanceAmount.setCellValueFactory(new PropertyValueFactory<TableVariables.Balance, String>("amount"));
        BalanceFreeze.setCellValueFactory(new PropertyValueFactory<TableVariables.Balance, String>("freeze"));
        BalanceWithdrawable.setCellValueFactory(new PropertyValueFactory<TableVariables.Balance, String>("withdrawable"));
        BalanceWorthInUSD.setCellValueFactory(new PropertyValueFactory<TableVariables.Balance, String>("worthofusd"));

    }
    @FXML
    void initialize() {

        setCellValue();
        addEventHandler();
    }
}