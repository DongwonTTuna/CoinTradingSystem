package com.coinTradingSystem.CoreController.EventHandler;

import com.coinTradingSystem.Main;
import com.coinTradingSystem.UI.ControlOrder.ControlOrder;
import com.coinTradingSystem.CoreController.CoreController;
import com.coinTradingSystem.CoreController.OrderTabEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Objects;


public class OrderWindowEvent implements OrderTabEvent {
    public CoreController coreController;




    public void OpenOrderWindowEvent(HashMap<String, String> HashData) throws IOException {
        if (Objects.equals(coreController.exchangeHandler.exchange.isAPINone, "0")) {OpenAlert("APIKEYが追加されてません");return;}
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/ControlOrder.fxml")));
        Parent root = loader.load();
        ControlOrder order = loader.getController();
        Stage pStage = new Stage();

        pStage.setTitle("オーダー追加");
        pStage.setScene(new Scene(root, 600, 400));
        pStage.setResizable(false);
        pStage.initModality(Modality.WINDOW_MODAL);
        pStage.initOwner(coreController.mainFrame.LoginButton.getScene().getWindow());


        onSymbolSelectedTabClicked(coreController, order);
        onOrderSubminButtonClicked(coreController, order, HashData, pStage);
        onMarketTradeToggled(order, coreController);
        setOrderTypeTrigger(order,coreController);

        if (HashData != null) {
            pStage.setTitle("オーダー編集");
            order.UUIDLabel.setText(HashData.get("uuid"));
            order.ExchangeLabel.setText(Main.CurrentExchange);
            order.SymbolSelectTab.getSelectionModel().select(HashData.get("symbol"));
            order.TargetPrice.setText(String.format("%.8f",new BigDecimal(HashData.get("targetPrice"))));
            order.Amount.setText(String.format("%.8f",new BigDecimal(HashData.get("amount"))));
            order.TriggerPrice.setText(String.format("%.8f",new BigDecimal(HashData.get("triggerPrice"))));
            order.LastestPrice.setText(coreController.exchangeHandler.exchange.getOneTickerPrice(HashData.get("symbol")).getLast().toString());
            switch (HashData.get("orderType")) {
                case "購入" -> order.OrderTypeBuy.setSelected(true);
                case "販売" -> order.OrderTypeSell.setSelected(true);
                case "損切" -> order.OrderTypeLossCut.setSelected(true);
                case "利益補填" -> order.OrderTypeTakeProfit.setSelected(true);
            }
        }

        coreController.callBackFunctions.WaitTilExchangeTickersUpdated();
        order.SymbolSelectTab.getItems().addAll(coreController.exchangeHandler.exchange.AllTickers.toArray(new String[0]));

        pStage.show();
    }
}
