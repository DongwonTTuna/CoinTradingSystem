package com.coinTradingSystem.UI;

import com.coinTradingSystem.HandleExchange.Binance;
import com.coinTradingSystem.Main;
import com.coinTradingSystem.SqlQuery;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MainFrame {
    connectAPI connectapi;
    @FXML
    private Label ExchangeStatus;
    @FXML
    private Label TotalBalance;
    @FXML
    private Label OpenOrders;
    @FXML
    private Label TotalTrades;
    @FXML
    private Label TodayProfit;
    @FXML
    private ToggleGroup Exchanges;
    @FXML
    private Button LoginButton;

    @FXML
    private Button RemoveOne;
    @FXML
    private Button AddOrder;
    @FXML
    private Button RemoveAll;
    @FXML
    private TextArea LogBox;

    private SqlQuery sql;

    void BinanceSetup() throws ExecutionException, InterruptedException {
        Binance bnance = new Binance();
        if (Objects.equals(bnance.apiKey, "null")) return;
        CompletableFuture.supplyAsync(bnance::getAllTickers);
        CompletableFuture.supplyAsync(bnance::isServerOnline).thenAcceptAsync((s)->{
            switch (s) {
                case "normal" -> Platform.runLater(()->ExchangeStatus.setText("OK"));
                case "system_maintenance" -> Platform.runLater(()->ExchangeStatus.setText("In Maintenance"));
                default -> Platform.runLater(()->ExchangeStatus.setText("Cannot to connect to the server."));
            }
        });
        CompletableFuture.supplyAsync(bnance::getAccountBalance).thenAcceptAsync((s)->{
            Platform.runLater(()->TotalBalance.setText(String.format("%.8f",s)));
        });
        CompletableFuture.supplyAsync(bnance::getOCONum).thenAcceptAsync((s)->{
            Platform.runLater(()->OpenOrders.setText(s.toString()));
        });
        CompletableFuture.supplyAsync(()-> SqlQuery.getTradesNum("BINANCE")).thenAcceptAsync((s)->{
            switch(s){
                case -1 -> Platform.runLater(()->TotalTrades.setText("None"));
                case 0 -> Platform.runLater(()->TotalTrades.setText(s.toString()));
            }
        });
        System.out.println("yes");
    }

    @FXML
    void initialize(){
        LogBox.setEditable(false);

        if (Objects.equals(Main.CurrentExchange, "BINANCE")){
            try {
                BinanceSetup();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        /*
        int a = bnance.getExchangeStatus();
        System.out.println(a);
        if (a == 0) ExchangeStatus.setText("ok");
        else ExchangeStatus.setText("None");*/
    }

    @FXML

    protected void onLoginButtonClicked(ActionEvent event) throws IOException {
        System.out.println(getClass().getResource("/LoginToApi.fxml"));
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/LoginToApi.fxml")));
        Parent root = loader.load();

        connectAPI login = loader.getController();

        login.LoginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                RadioButton SelectedRB = (RadioButton)login.Exchange.getSelectedToggle();
                String exchange =  SelectedRB.getText();
                String api_key = login.API_KEY.getText();
                String secret_key = login.SECRET_KEY.getText();
                Main.setAPI(exchange,api_key,secret_key);
                if (login.toDatabase.isSelected()){
                    SqlQuery.updateAPI(exchange,api_key,secret_key);
                }
                try {
                    BinanceSetup();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });


        Stage pStage = new Stage();
        pStage.setTitle("API 接続 Window");
        pStage.setScene(new Scene(root,600,400));
        pStage.setResizable(false);
        pStage.initModality(Modality.WINDOW_MODAL);
        pStage.initOwner(LoginButton.getScene().getWindow());

        pStage.show();
    }

    protected void onOrderRowDoubleClicked(MouseEvent event) throws IOException{

    }
    @FXML
    protected void onAddButtonClicked(MouseEvent event) throws IOException{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ControlOrder.fxml")));
        Stage pStage = new Stage();
        pStage.setTitle("オーダ追加");
        pStage.setScene(new Scene(root,600,800));
        pStage.setResizable(false);
        pStage.initModality(Modality.WINDOW_MODAL);
        pStage.initOwner(LoginButton.getScene().getWindow());
        pStage.show();
    }

    @FXML
    protected void onRemoveButtonClicked(MouseEvent event){

    }
    @FXML
    protected void onRemoveAllButtonClicked(MouseEvent event){

    }

}