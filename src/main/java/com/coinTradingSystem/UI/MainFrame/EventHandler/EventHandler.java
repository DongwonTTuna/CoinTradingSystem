package com.coinTradingSystem.UI.MainFrame.EventHandler;

import com.coinTradingSystem.CoreController.InterFace.AddLog;
import com.coinTradingSystem.Main;
import com.coinTradingSystem.SqlQuery;
import com.coinTradingSystem.UI.Connect.connectAPI;
import com.coinTradingSystem.UI.MainFrame.Variables;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class EventHandler extends Variables {
    public void OnDividerMove(SplitPane dr) {
        SplitPane.Divider d = dr.getDividers().get(0);
        d.positionProperty().addListener(changeListner -> {
            d.setPosition(0.5);
        });
    }

    private void ConnectionLogUpdate(){
        CompletableFuture.runAsync(() -> {
            try {
                Main.Exchange.WaitTilUserAccountBalances();
                Platform.runLater(() -> ConnectionLog.setText("接続が取れました"));
            } catch (Exception e) {
                Platform.runLater(() -> ConnectionLog.setText("接続が取れませんでした"));
            }
        });
    }
    private void ChangeTabValue() {
        ConnectionLog.setText(Main.Exchange.getExchangeName() + "に接続中");
        switch (Main.Exchange.getExchangeName()) {
            case "BINANCE" -> {OrderWorthInUsd.setText("Worth In USDT");BalanceWorthInUSD.setText("Worth In USDT");}
            case "GATEIO" -> {OrderWorthInUsd.setText("Worth In USDT");BalanceWorthInUSD.setText("Worth In USDT");}
            case "UPBIT" -> {OrderWorthInUsd.setText("Worth In KRW");BalanceWorthInUSD.setText("Worth In KRW");}
        }
        ConnectionLogUpdate();
    }
    public void initializeMainframe(){
        ExchangeStatus.setText("Fetching...");
        TotalBalance.setText("Fetching...");
        OpenOrders.setText("Fetching...");
        TotalTrades.setText("Fetching...");
        TodayProfit.setText("Fetching...");
        BalanceTable.getItems().clear();
        TB = 0.0;
        TotalBalanceInUSD.setText("0");
        LogBox.getChildren().clear();
    }
    public void OnExchangeChanged(RadioButton o) {
        o.setOnAction(actionEvent -> {
            Main.Exchange = Main.coreController.AllExchanges.get(o.getText());
            initializeMainframe();
            ChangeTabValue();
        });
    }

    public void AddLoginEvent(Button b) {
        b.setOnAction(actionEvent -> {
            try {
                onLoginButtonClicked();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void onLoginButtonClicked() throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/LoginToApi.fxml")));
        Parent root = loader.load();

        connectAPI login = loader.getController();
        login.LoginButton.setOnAction(actionEvent -> {
            RadioButton SelectedRB = (RadioButton) login.Exchange.getSelectedToggle();
            String exchange = SelectedRB.getText();
            String api_key = login.API_KEY.getText();
            String secret_key = login.SECRET_KEY.getText();
            Main.setAPI(exchange, api_key, secret_key);
            if (login.toDatabase.isSelected()) {
                SqlQuery.updateAPI(exchange, api_key, secret_key);
            }
            login.API_KEY.setText("");
            login.SECRET_KEY.setText("");
        });


        Stage pStage = new Stage();
        pStage.setTitle("API 接続 Window");
        pStage.setScene(new Scene(root, 600, 400));
        pStage.setResizable(false);
        pStage.initModality(Modality.WINDOW_MODAL);
        pStage.initOwner(LoginButton.getScene().getWindow());

        pStage.show();
    }

    public void addEventHandler() {
        OnExchangeChanged(ExchangeB);
        OnExchangeChanged(ExchangeG);
        OnExchangeChanged(ExchangeM);
        AddLoginEvent(LoginButton);
        OnDividerMove(Splitpane);
    }
}
