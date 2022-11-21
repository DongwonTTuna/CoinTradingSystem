package com.coinTradingSystem.UI.MainFrame.CoreController.EventHandler;

import com.coinTradingSystem.Main;
import com.coinTradingSystem.SqlQuery;
import com.coinTradingSystem.UI.ControlOrder.ControlOrder;
import com.coinTradingSystem.UI.MainFrame.CoreController.CoreController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;


public class OrderWindowEvent {
    public CoreController coreController;

    protected String getOrderTypeString(String num){
        switch(num){
            case "0" -> {return "購入";}
            case "1" -> {return "販売";}
            case "2" -> {return "損切";}
            case "3" -> {return "利益補填";}
        }
        throw new RuntimeException();
    }
    private short getOrderTypeShort(String num){
        switch(num){
            case "購入" -> {return 0;}
            case "売却" -> {return 1;}
            case "損切" -> {return 2;}
            case "利益補填" -> {return 3;}

        }
        throw new RuntimeException();
    }
    private void OpenAlert(String contentText){
        Alert alrt = new Alert(Alert.AlertType.WARNING);
        alrt.setTitle("エアー");
        alrt.setHeaderText("エアーが発生しました");
        alrt.setContentText(contentText);
        alrt.showAndWait();
    }

    private double CheckTypeIsDouble(String str, String msg){
        try{
             return Double.parseDouble(str);
        } catch (Exception e){
            OpenAlert(msg + "を確認してください。");
        }
        throw new RuntimeException();
    }
    public void OpenOrderWindowEvent(HashMap<String, String> HashData) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/ControlOrder.fxml")));
        Parent root = loader.load();
        ControlOrder order = loader.getController();
        Stage pStage = new Stage();

        pStage.setTitle("オーダー追加");
        pStage.setScene(new Scene(root,600,400));
        pStage.setResizable(false);
        pStage.initModality(Modality.WINDOW_MODAL);
        pStage.initOwner(coreController.mainFrame.LoginButton.getScene().getWindow());

        order.Submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String symbol = order.SymbolSelectTab.getSelectionModel().selectedItemProperty().getValue();
                if (order.OrderType.getSelectedToggle() == null) {OpenAlert("オーダのタイプを指定してください。");return;};
                double TargetPrice = CheckTypeIsDouble(order.TargetPrice.getText(),"目標価格"),
                        TriggerPrice = CheckTypeIsDouble(order.TriggerPrice.getText(),"トリガ"),
                        Amount = CheckTypeIsDouble(order.Amount.getText(),"数量");
                if (HashData != null) {SqlQuery.updateOrder(order.UUIDLabel.getText(),Main.CurrentExchange,getOrderTypeShort(((RadioButton) order.OrderType.getSelectedToggle()).getText()),symbol,TriggerPrice,TargetPrice,Amount);coreController.controlValue.AddLog("オーダーが編集されました");}
                else {SqlQuery.addOrder(Main.CurrentExchange,getOrderTypeShort(((RadioButton) order.OrderType.getSelectedToggle()).getText()),symbol,TriggerPrice,TargetPrice,Amount);coreController.controlValue.AddLog("オーダーが追加されました");}
                coreController.orderTable.InitializeOrderTableView();
                pStage.close();
            }
        });


        if (HashData != null){
            pStage.setTitle("オーダー編集");
            order.UUIDLabel.setText(HashData.get("uuid"));
            order.ExchangeLabel.setText(Main.CurrentExchange);
            order.SymbolSelectTab.getSelectionModel().select(HashData.get("symbol"));
            order.TargetPrice.setText(HashData.get("targetPrice"));
            order.Amount.setText(HashData.get("amount"));
            order.TriggerPrice.setText(HashData.get("checkPrice"));
            switch (HashData.get("orderType")){
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
