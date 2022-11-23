package com.coinTradingSystem.CoreController;

import com.coinTradingSystem.Main;
import com.coinTradingSystem.SqlQuery;
import com.coinTradingSystem.UI.ControlOrder.ControlOrder;
import com.coinTradingSystem.UI.MainFrame.TableVariables;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Objects;

public interface OrderTabEvent {
    default void OpenAlert(String contentText) {
        Alert alrt = new Alert(Alert.AlertType.WARNING);
        alrt.setTitle("エアー");
        alrt.setHeaderText("エアーが発生しました");
        alrt.setContentText(contentText);
        alrt.showAndWait();
    }

    default String getCutStringPrice(String text){
        double tempDouble = Double.parseDouble(text);
        if (tempDouble == 0.0) return text;
        else if (tempDouble >= 0.1) return String.format("%.3f",tempDouble);
        else if (tempDouble >= 0.01) return String.format("%.4f",tempDouble);
        else if (tempDouble >= 0.001) return String.format("%.5f",tempDouble);
        else if (tempDouble >= 0.0001) return String.format("%.6f",tempDouble);
        else if (tempDouble >= 0.00001) return String.format("%.7f",tempDouble);
        else if (tempDouble >= 0.000001) return String.format("%.8f",tempDouble);
        else if (tempDouble >= 0.0000001) return String.format("%.9f",tempDouble);
        else if (tempDouble >= 0.00000001) return String.format("%.10f",tempDouble);
        else if (tempDouble == 0) return "0";
        throw new RuntimeException();
    }
    default String getCutStringAmount(String text){
        double tempDouble = Double.parseDouble(text);
        if (tempDouble > 1) return String.format("%.0f",tempDouble);
        if (tempDouble >= 0.1) return String.format("%.3f",tempDouble);
        else if (tempDouble >= 0.01) return String.format("%.4f",tempDouble);
        else if (tempDouble >= 0.001) return String.format("%.5f",tempDouble);
        else if (tempDouble >= 0.0001) return String.format("%.6f",tempDouble);
        else if (tempDouble >= 0.00001) return String.format("%.7f",tempDouble);
        else if (tempDouble >= 0.000001) return String.format("%.8f",tempDouble);
        else if (tempDouble >= 0.0000001) return String.format("%.9f",tempDouble);
        else if (tempDouble >= 0.00000001) return String.format("%.10f",tempDouble);
        else if (tempDouble == 0) return "0";
        throw new RuntimeException();
    }
    default String getOrderTypeString(String num) {
        switch (num) {
            case "0" -> {
                return "購入";
            }
            case "1" -> {
                return "販売";
            }
            case "2" -> {
                return "損切";
            }
            case "3" -> {
                return "利益補填";
            }
        }
        throw new RuntimeException();
    }

    private short getOrderTypeShort(String num) {
        switch (num) {
            case "購入" -> {
                return 0;
            }
            case "売却" -> {
                return 1;
            }
            case "損切" -> {
                return 2;
            }
            case "利益補填" -> {
                return 3;
            }

        }
        throw new RuntimeException();
    }
    private BigDecimal CheckTypeIsBigDecimal(String str, String msg) {
        try {
            return new BigDecimal(str);
        } catch (Exception e) {
            OpenAlert(msg + "を確認してください。");
        }
        throw new RuntimeException();
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
    private void onBuy(BigDecimal price, ControlOrder order){
        if (price == null) return;
        Platform.runLater(()->order.LastestPrice.setText(price.toString()));
        Platform.runLater(()-> order.TriggerPrice.setText(price.toString()));
        if(!order.MarketTrade.isSelected()) Platform.runLater(()->order.TargetPrice.setText(price.add(price.multiply(new BigDecimal("0.01"))).toString()));
    }
    private void onSell(BigDecimal price, ControlOrder order){
        if (price == null) return;
        Platform.runLater(()->order.LastestPrice.setText(price.toString()));
        Platform.runLater(()-> order.TriggerPrice.setText(price.toString()));
        if(!order.MarketTrade.isSelected()) Platform.runLater(()->order.TargetPrice.setText(price.subtract(price.multiply(new BigDecimal("0.01"))).toString()));
    }
    private void onLossCut(BigDecimal price, ControlOrder order){
        if (price == null) return;
        Platform.runLater(()->order.LastestPrice.setText(price.toString()));
        Platform.runLater(()-> order.TriggerPrice.setText(price.toString()));
        if(!order.MarketTrade.isSelected()) Platform.runLater(()->order.TargetPrice.setText(price.subtract(price.multiply(new BigDecimal("0.1"))).toString()));
    }
    private void onTakeProfit(BigDecimal price, ControlOrder order){
        if (price == null) return;
        Platform.runLater(()->order.LastestPrice.setText(price.toString()));
        Platform.runLater(()-> order.TriggerPrice.setText(price.toString()));
        Platform.runLater(()-> order.TargetPrice.setText(price.subtract(price.multiply(new BigDecimal("0.1"))).toString()));
    }
    private void onOrderTypeChanged(ControlOrder order, CoreController core){
        BigDecimal price = null;
        if (order.OrderType.getSelectedToggle() == null) return;
        if (order.SymbolSelectTab.getSelectionModel().getSelectedItem() != null){
            String symbol = order.SymbolSelectTab.getSelectionModel().getSelectedItem();
            price = core.exchangeHandler.exchange.getOneTickerPrice(symbol).getLast();
        }
        switch (((RadioButton) order.OrderType.getSelectedToggle()).getText()){
            case "購入" -> {onBuy(price, order);return;}
            case "売却" ->{onSell(price, order);return;}
            case "損切" ->{onLossCut(price, order);return;}
            case "利益補填" -> {onTakeProfit(price,order);return;}
        }
        throw new RuntimeException();
    }
    private void updateOnOffOfVariables(ControlOrder order, CoreController core){
        if(Objects.equals(((RadioButton) order.OrderType.getSelectedToggle()).getText(), "利益補填")){
            core.eventHandler.OrderPrevChecked = order.MarketTrade.isSelected();
            order.MarketTrade.setSelected(true);
            order.MarketTrade.setDisable(true);
            order.TargetPrice.setDisable(false);
            order.TriggerPrice.setDisable(false);
        }else {
            order.MarketTrade.setSelected(core.eventHandler.OrderPrevChecked);
            order.MarketTrade.setDisable(false);
            order.TargetPrice.setDisable(false);
            if(order.MarketTrade.isSelected()){
                order.TargetPrice.setDisable(true);
            }
        }
    }

    default void onSymbolSelectedTabClicked(CoreController coreController, ControlOrder order){
        order.SymbolSelectTab.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                onOrderTypeChanged(order,coreController);
            }
        });
    }

    default void onOrderSubminButtonClicked(CoreController coreController, ControlOrder order, HashMap<String, String> HashData, Stage pStage){
        order.Submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String symbol = order.SymbolSelectTab.getSelectionModel().selectedItemProperty().getValue();
                if (order.OrderType.getSelectedToggle() == null) {OpenAlert("オーダのタイプを指定してください。");return;};
                BigDecimal TargetPrice = order.MarketTrade.isSelected() ? BigDecimal.ZERO : CheckTypeIsBigDecimal(order.TargetPrice.getText(),"目標価格"),
                        TriggerPrice = CheckTypeIsBigDecimal(order.TriggerPrice.getText(),"トリガ"),
                        Amount = CheckTypeIsBigDecimal(order.Amount.getText(),"数量");
                if (HashData != null) {
                    SqlQuery.updateOrder(order.UUIDLabel.getText(), Main.CurrentExchange,getOrderTypeShort(((RadioButton) order.OrderType.getSelectedToggle()).getText()),symbol,TriggerPrice,TargetPrice,Amount);coreController.controlValue.AddLog("オーダーが編集されました");}
                else {SqlQuery.addOrder(Main.CurrentExchange,getOrderTypeShort(((RadioButton) order.OrderType.getSelectedToggle()).getText()),symbol,TriggerPrice,TargetPrice,Amount);coreController.controlValue.AddLog("オーダーが追加されました");}
                coreController.orderTable.InitializeOrderTableView();
                pStage.close();
            }
        });
    }
    default void onMarketTradeToggled(ControlOrder order,CoreController core){
        order.MarketTrade.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                core.eventHandler.OrderPrevChecked = order.MarketTrade.isSelected();
                order.TargetPrice.setText("");
                order.TargetPrice.setDisable(order.MarketTrade.isSelected());
            }
        });
    }
    default void setOrderTypeTrigger(ControlOrder order, CoreController core){
        order.OrderTypeSell.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                updateOnOffOfVariables(order,core);
                if(order.SymbolSelectTab.getSelectionModel().getSelectedItem() != null){
                    BigDecimal price = core.exchangeHandler.exchange.getOneTickerPrice(order.SymbolSelectTab.getSelectionModel().getSelectedItem()).getLast();
                    order.LastestPrice.setText(price.toString());
                    order.TriggerPrice.setText(price.add(price.multiply(new BigDecimal("0.1"))).toString());
                    if(!order.MarketTrade.isSelected()) order.TargetPrice.setText(price.add(price.multiply(new BigDecimal("0.09"))).toString());
                }
            }
        });

        order.OrderTypeBuy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                updateOnOffOfVariables(order,core);
                onOrderTypeChanged(order, core);
            }
        });
        order.OrderTypeLossCut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                updateOnOffOfVariables(order,core);
                onOrderTypeChanged(order, core);
            }
        });
        order.OrderTypeTakeProfit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                updateOnOffOfVariables(order,core);
                onOrderTypeChanged(order, core);
            }
        });
    }
}
