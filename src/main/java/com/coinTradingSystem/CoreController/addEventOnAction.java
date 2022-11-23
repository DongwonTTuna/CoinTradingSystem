package com.coinTradingSystem.CoreController;

import com.coinTradingSystem.Main;
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

    default void SetupTheTab(String ChangedTabText, CoreController core) {
        switch (ChangedTabText) {
            case "Status" ->
                    core.exchangeHandler.UpdateStatusTabVariables();

            case "Balances" ->
                    CompletableFuture.runAsync(() -> core.exchangeHandler.UpdateBalanceTabVariables());

            case "Orders" ->
                    CompletableFuture.runAsync(() -> core.exchangeHandler.UpdateOrdersTabVariables());
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



}
