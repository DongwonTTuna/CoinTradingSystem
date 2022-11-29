package com.coinTradingSystem.UI.MainFrame.Tab.EventHandler.OrderWindowFunctions;

import com.coinTradingSystem.CoreController.Exchange.ExchangeMain;
import com.coinTradingSystem.CoreController.InterFace.Alert;
import com.coinTradingSystem.Main;
import com.coinTradingSystem.UI.ControlOrder.ControlOrder;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Objects;

public class OpenOrderWindowHandler extends Functions implements Alert {

    public void OpenOrderWindowEvent(HashMap<String, String> HashData) throws IOException {
        ExchangeMain exData = Main.Exchange;
        if (Objects.equals(exData.getAPIStatus(), "0")) {OpenAlert("APIKEYが追加されてません");return;}

        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/ControlOrder.fxml")));
        Parent root = loader.load();
        this.controlOrder = loader.getController();
        Stage pStage = new Stage();

        pStage.setTitle("オーダー追加");
        pStage.setScene(new Scene(root, 600, 400));
        pStage.setResizable(false);
        pStage.initModality(Modality.WINDOW_MODAL);
        pStage.initOwner(Main.mainFrame.LoginButton.getScene().getWindow());


        onSymbolSelectedTabClicked();
        onOrderSubminButtonClicked(HashData, pStage);
        onMarketTradeToggled();
        setOrderTypeTrigger();

        if (HashData != null) {
            pStage.setTitle("オーダー編集");
            controlOrder.UUIDLabel.setText(HashData.get("uuid"));
            controlOrder.ExchangeLabel.setText(Main.Exchange.getExchangeName());
            controlOrder.SymbolSelectTab.getSelectionModel().select(HashData.get("symbol"));
            controlOrder.TargetPrice.setText(String.format("%.8f",new BigDecimal(HashData.get("targetPrice"))));
            controlOrder.Amount.setText(String.format("%.8f",new BigDecimal(HashData.get("amount"))));
            controlOrder.TriggerPrice.setText(String.format("%.8f",new BigDecimal(HashData.get("triggerPrice"))));
            controlOrder.LastestPrice.setText(exData.getOneTicker(HashData.get("symbol")).getLast().toString());
            switch (HashData.get("controlOrderType")) {
                case "購入" -> controlOrder.OrderTypeBuy.setSelected(true);
                case "販売" -> controlOrder.OrderTypeSell.setSelected(true);
                case "損切" -> controlOrder.OrderTypeLossCut.setSelected(true);
                case "利益補填" -> controlOrder.OrderTypeTakeProfit.setSelected(true);
            }
        }

        exData.WaitTilAllTickers();
        controlOrder.SymbolSelectTab.getItems().addAll(exData.getAllTickers().toArray(new String[0]));

        pStage.show();
    }

}
