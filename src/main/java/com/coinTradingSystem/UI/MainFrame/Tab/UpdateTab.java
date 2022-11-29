package com.coinTradingSystem.UI.MainFrame.Tab;

import com.coinTradingSystem.CoreController.CoreController;
import com.coinTradingSystem.CoreController.InterFace.FormatString;
import com.coinTradingSystem.Main;
import com.coinTradingSystem.UI.MainFrame.MainFrame;
import javafx.scene.control.Tab;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class UpdateTab implements FormatString {
    private final StatusTab statusTab;
    private final BalanceTab balanceTab;
    private final OrderTab orderTab;

    public void OnTabChanged(Tab t) {
        t.setOnSelectionChanged(actionEvent ->{
            String ChangedTabText = Main.mainFrame.InfoTab.getSelectionModel().getSelectedItem().getText();
            if (!Objects.equals(Main.CurrentTabText, ChangedTabText)) {
                Main.CurrentTabText = ChangedTabText;
                SetupTheTab(ChangedTabText);
            }});
    }
    public void SetupTheTab(String ChangedTabText) {
        switch (ChangedTabText) {
            case "Status" -> statusTab.UpdateStatusTabVariables();

            case "Balances" ->
                    CompletableFuture.runAsync(balanceTab::UpdateBalanceTabVariables);

            case "Orders" ->
                    CompletableFuture.runAsync(orderTab::InitializeOrderTableView);
        }
    }

    public void UpdateAllTabs(){
        statusTab.UpdateStatusTabVariables();
        balanceTab.UpdateBalanceTabVariables();
        orderTab.InitializeOrderTableView();
    }

    public UpdateTab() {
        statusTab = new StatusTab();
        balanceTab = new BalanceTab();
        orderTab = new OrderTab();
        OnTabChanged(Main.mainFrame.BalanceTab);
        OnTabChanged(Main.mainFrame.OrderTab);
        OnTabChanged(Main.mainFrame.StatusTab);
    }
}
