package com.coinTradingSystem.UI.MainFrame.Tab.EventHandler.OrderWindowFunctions;

import com.coinTradingSystem.CoreController.InterFace.AddLog;
import com.coinTradingSystem.CoreController.InterFace.Alert;
import com.coinTradingSystem.CoreController.InterFace.CheckOrderVariablesType;
import com.coinTradingSystem.Main;
import com.coinTradingSystem.SqlQuery;
import com.coinTradingSystem.UI.ControlOrder.ControlOrder;
import javafx.application.Platform;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Objects;

public class Functions implements Alert, CheckOrderVariablesType, AddLog {
    private boolean OrderPrevChecked;
    public ControlOrder controlOrder;
    private void onBuy(BigDecimal price){
        if (price == null) return;
        Platform.runLater(()->controlOrder.LastestPrice.setText(price.toString()));
        Platform.runLater(()-> controlOrder.TriggerPrice.setText(price.toString()));
        if(!controlOrder.MarketTrade.isSelected()) Platform.runLater(()->controlOrder.TargetPrice.setText(price.add(price.multiply(new BigDecimal("0.01"))).toString()));
    }
    private void onSell(BigDecimal price){
        if (price == null) return;
        Platform.runLater(()->controlOrder.LastestPrice.setText(price.toString()));
        Platform.runLater(()-> controlOrder.TriggerPrice.setText(price.toString()));
        if(!controlOrder.MarketTrade.isSelected()) Platform.runLater(()->controlOrder.TargetPrice.setText(price.subtract(price.multiply(new BigDecimal("0.01"))).toString()));
    }
    private void onLossCut(BigDecimal price){
        if (price == null) return;
        Platform.runLater(()->controlOrder.LastestPrice.setText(price.toString()));
        Platform.runLater(()-> controlOrder.TriggerPrice.setText(price.toString()));
        if(!controlOrder.MarketTrade.isSelected()) Platform.runLater(()->controlOrder.TargetPrice.setText(price.subtract(price.multiply(new BigDecimal("0.1"))).toString()));
    }
    private void onTakeProfit(BigDecimal price) {
        if (price == null) return;
        Platform.runLater(()->controlOrder.LastestPrice.setText(price.toString()));
        Platform.runLater(()-> controlOrder.TriggerPrice.setText(price.toString()));
        Platform.runLater(()-> controlOrder.TargetPrice.setText(price.subtract(price.multiply(new BigDecimal("0.1"))).toString()));
    }
    private void onOrderTypeChanged(){
        BigDecimal price = null;
        if (controlOrder.OrderType.getSelectedToggle() == null) return;
        if (controlOrder.SymbolSelectTab.getSelectionModel().getSelectedItem() != null){
            String symbol = controlOrder.SymbolSelectTab.getSelectionModel().getSelectedItem();
            price = Main.Exchange.getOneTicker(symbol).getLast();
        }
        switch (((RadioButton) controlOrder.OrderType.getSelectedToggle()).getText()){
            case "購入" -> {onBuy(price);return;}
            case "売却" ->{onSell(price) ;return;}
            case "損切" ->{onLossCut(price) ;return;}
            case "利益補填" -> {onTakeProfit(price);return;}
        }
        throw new RuntimeException();
    }
    private void updateOnOffOfVariables(){
        if(Objects.equals(((RadioButton) controlOrder.OrderType.getSelectedToggle()).getText(), "利益補填")){
            OrderPrevChecked = controlOrder.MarketTrade.isSelected();
            controlOrder.MarketTrade.setSelected(true);
            controlOrder.MarketTrade.setDisable(true);
            controlOrder.TargetPrice.setDisable(false);
            controlOrder.TriggerPrice.setDisable(false);
        }else {
            controlOrder.MarketTrade.setSelected(OrderPrevChecked);
            controlOrder.MarketTrade.setDisable(false);
            controlOrder.TargetPrice.setDisable(false);
            if(controlOrder.MarketTrade.isSelected()){
                controlOrder.TargetPrice.setDisable(true);
            }
        }
    }

    public void onSymbolSelectedTabClicked(){
        controlOrder.SymbolSelectTab.setOnAction(actionEvent -> {
            onOrderTypeChanged();
        });
    }

    public void onOrderSubminButtonClicked(HashMap<String, String> HashData, Stage pStage){
        controlOrder.Submit.setOnAction(actionEvent -> {
            String symbol = controlOrder.SymbolSelectTab.getSelectionModel().selectedItemProperty().getValue();
            if (controlOrder.OrderType.getSelectedToggle() == null) {OpenAlert("オーダのタイプを指定してください。");return;};
            BigDecimal TargetPrice = controlOrder.MarketTrade.isSelected() ? BigDecimal.ZERO : CheckTypeIsBigDecimal(controlOrder.TargetPrice.getText(),"目標価格"),
                    TriggerPrice = CheckTypeIsBigDecimal(controlOrder.TriggerPrice.getText(),"トリガ"),
                    Amount = CheckTypeIsBigDecimal(controlOrder.Amount.getText(),"数量");
            if (HashData != null) {
                SqlQuery.updateOrder(controlOrder.UUIDLabel.getText(), Main.Exchange.getExchangeName(),getOrderTypeShort(((RadioButton) controlOrder.OrderType.getSelectedToggle()).getText()),symbol,TriggerPrice,TargetPrice,Amount);AddLog("オーダーが編集されました");}
            else {SqlQuery.addOrder(Main.Exchange.getExchangeName(),getOrderTypeShort(((RadioButton) controlOrder.OrderType.getSelectedToggle()).getText()),symbol,TriggerPrice,TargetPrice,Amount);AddLog("オーダーが追加されました");}
            Main.mainFrame.updateTab.UpdateAllTabs();
            pStage.close();
        });
    }
    public void onMarketTradeToggled() {
        controlOrder.MarketTrade.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent ->  {
            OrderPrevChecked = controlOrder.MarketTrade.isSelected();
            controlOrder.TargetPrice.setText("");
            controlOrder.TargetPrice.setDisable(controlOrder.MarketTrade.isSelected());
        });
    }
    public void setOrderTypeTrigger() {
        controlOrder.OrderTypeSell.setOnAction(actionEvent -> {
            updateOnOffOfVariables();
            if(controlOrder.SymbolSelectTab.getSelectionModel().getSelectedItem() != null){
                BigDecimal price = Main.Exchange.getOneTicker(controlOrder.SymbolSelectTab.getSelectionModel().getSelectedItem()).getLast();
                controlOrder.LastestPrice.setText(price.toString());
                controlOrder.TriggerPrice.setText(price.add(price.multiply(new BigDecimal("0.1"))).toString());
                if(!controlOrder.MarketTrade.isSelected()) controlOrder.TargetPrice.setText(price.add(price.multiply(new BigDecimal("0.09"))).toString());
            }
        });

        controlOrder.OrderTypeBuy.setOnAction(actionEvent -> {
            updateOnOffOfVariables();
            onOrderTypeChanged();
        });
        controlOrder.OrderTypeLossCut.setOnAction(actionEvent -> {
            updateOnOffOfVariables();
            onOrderTypeChanged();
        });
        controlOrder.OrderTypeTakeProfit.setOnAction(actionEvent -> {
            updateOnOffOfVariables();
            onOrderTypeChanged();
        });
    }

}
