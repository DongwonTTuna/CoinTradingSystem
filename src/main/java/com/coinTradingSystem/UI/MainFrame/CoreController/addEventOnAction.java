package com.coinTradingSystem.UI.MainFrame.CoreController;

import com.coinTradingSystem.Main;
import com.coinTradingSystem.SqlQuery;
import com.coinTradingSystem.UI.MainFrame.TableVariables;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public interface addEventOnAction {
    default void OnExchangeChanged(RadioButton o, CoreController core) {
        o.setOnAction(new javafx.event.EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                RadioButton selected = (RadioButton) core.mainFrame.Exchanges.getSelectedToggle();
                Main.CurrentExchange = selected.getText();
                core.exchangeHandler.ExchangeFunction();
            }
        });
    }

    default void AddLoginEvent(Button b, CoreController core) {
        b.setOnAction(new javafx.event.EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    core.eventHandler.onLoginButtonClicked();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    default void OnTabChanged(Tab t, CoreController core) {
        t.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                String ChangedTabText = core.mainFrame.InfoTab.getSelectionModel().getSelectedItem().getText();
                if (!Objects.equals(Main.CurrentTabText, ChangedTabText)) {
                    Main.CurrentTabText = ChangedTabText;
                    SetupTheTab(ChangedTabText, core);
                }
            }
        });
    }

    private void SetupTheTab(String ChangedTabText, CoreController core) {
        switch (ChangedTabText) {
            case "Status" ->
                    CompletableFuture.runAsync(() -> core.callBackFunctions.WaitTilExchangeInitializeDone()).thenAcceptAsync((s) -> core.exchangeHandler.UpdateStatusTabVariables());

            case "Balances" ->
                    CompletableFuture.runAsync(() -> core.callBackFunctions.WaitTilExchangeInitializeDone()).thenAcceptAsync((s) -> core.exchangeHandler.UpdateBalanceTabVariables());

            case "Orders" ->
                    CompletableFuture.runAsync(() -> core.callBackFunctions.WaitTilExchangeInitializeDone()).thenAcceptAsync((s) -> core.exchangeHandler.UpdateOrdersTabVariables());
        }
    }

    default void OnDividerMove(SplitPane dr) {
        SplitPane.Divider d = dr.getDividers().get(0);
        d.positionProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                d.setPosition(0.5);
            }
        });
    }


    default void onRemoveButtonClicked(Button btn, CoreController core) {
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                TableVariables.Order order = core.mainFrame.OrderTable.getSelectionModel().getSelectedItem();
                if (order == null) return;
                String uuid = order.getUuid();
                SqlQuery.removeOrder(uuid);
                core.orderTable.InitializeOrderTableView();
                core.controlValue.AddLog("オーダーが削除されました");
            }
        });
    }

    default void onRemoveAllButtonClicked(Button btn, CoreController core) {
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                SqlQuery.removeAllOrders();
                core.orderTable.InitializeOrderTableView();
                core.controlValue.AddLog("全てのオーダーが削除されました");
            }
        });
    }

    default void onAddOrderButtonClicked(Button btn, CoreController core) {
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    core.orderTable.OpenOrderWindowEvent(null);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
