package com.coinTradingSystem.UI;

import com.coinTradingSystem.HandleExchange.Binance.Binance;
import com.coinTradingSystem.Main;
import com.coinTradingSystem.SqlQuery;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static org.awaitility.Awaitility.await;


public class MainFrame {
    public static class Balance{
        private final String symbol;
        private final String amount;
        private final String freeze;
        private final String withdrawable;
        private final String worthofusd;

        public Balance(String symbol, String amount, String freeze, String withdrawable, String worthofusd) {
            this.symbol = symbol;
            this.amount = amount;
            this.freeze = freeze;
            this.withdrawable = withdrawable;
            this.worthofusd = worthofusd;
        }
        public String getSymbol(){return symbol;}
        public String getAmount(){return amount;}
        public String getFreeze(){return freeze;}
        public String getWithdrawable(){return withdrawable;}
        public String getWorthofusd(){return worthofusd;}
    }
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

    @FXML
    private TableView<Balance> BalanceTable;
    @FXML
    private TableColumn<Balance,String> BalanceSymbol;
    @FXML
    private TableColumn<Balance,String> BalanceAmount;
    @FXML
    private TableColumn<Balance,String> BalanceFreeze;
    @FXML
    private TableColumn<Balance,String> BalanceWithdrawable;
    @FXML
    private TableColumn<Balance,String> BalanceWorthInUSD;
    @FXML
    private Label TotalBalanceInUSD;

    private SqlQuery sql;


    private ArrayList<HashMap<String,String>> AllBalanceData;
    private double TB = 0.0;


    synchronized void BinanceSetup() {
        Binance bnance = new Binance();
        if (Objects.equals(bnance.apiKey, "null")) return;
        CompletableFuture.supplyAsync(bnance::isServerOnline).thenAcceptAsync((s)->{
            switch (s) {
                case "normal" -> Platform.runLater(()->ExchangeStatus.setText("OK"));
                case "system_maintenance" -> Platform.runLater(()->ExchangeStatus.setText("In Maintenance"));
                default -> Platform.runLater(()->ExchangeStatus.setText("Cannot to connect to the server."));
            }
        });
        CompletableFuture.supplyAsync(bnance::getAccountBalance).thenApplyAsync((s)->{
            Platform.runLater(()-> TotalBalance.setText(String.format("%.8f",s)));
            return bnance.getPbal();
        }).thenAcceptAsync((s)->{
            Platform.runLater(()->TodayProfit.setText(String.format("%.8f",Double.parseDouble(TotalBalance.getText()) - s)));
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
        CompletableFuture.supplyAsync(bnance::getAllcoinInfo).thenAcceptAsync((s)->{
            await().until(bnance.DoesInitializeDone());
            AllBalanceData = s;
            ObservableList<Balance> bal = BalanceTable.getItems();
            for (HashMap<String,String> item : AllBalanceData){
                String symbol = item.get("symbol");
                String amount = item.get("amount");
                double worthofusd = Double.parseDouble(amount)* Double.parseDouble(bnance.allTickersPrice.get(symbol+"USDT").toString());
                TB += worthofusd;
                Platform.runLater(()->TotalBalanceInUSD.setText(String.format("%.3f",TB)));
                bal.add(new Balance(
                        symbol,
                        amount,
                        item.get("freeze"),
                        item.get("withdrawable").toUpperCase(),
                        String.format("%.3f",worthofusd)
                ));
            }

            BalanceTable.setItems(bal);



        });
        /*CompletableFuture.supplyAsync(()->SqlQuery.getOrderList("BINANCE")).thenAcceptAsync((s)->{
            System.out.println(s);
        });*/

        System.out.println("yes");
    }

    void GateIOSetup() {

    }

    void MexcSetup(){

    }

    void HuobiSetup(){

    }

    private void InitializeAllValue(){
        ExchangeStatus.setText("Fetching...");
        TotalBalance.setText("Fetching...");
        OpenOrders.setText("Fetching...");
        TotalTrades.setText("Fetching...");
        TodayProfit.setText("Fetching...");
        BalanceTable.getItems().clear();

        TB = 0.0;
        TotalBalanceInUSD.setText("0");
    }

    private void ExchangeFunction(){
        InitializeAllValue();
        switch(Main.CurrentExchange){
            case "BINANCE" -> {
                try {
                    BinanceSetup();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            case "GATEIO" -> {
                try{

                    System.out.println("F");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    }
    @FXML
    void initialize(){
        LogBox.setEditable(false);
        BalanceSymbol.setCellValueFactory(new PropertyValueFactory<Balance,String>("symbol"));
        BalanceAmount.setCellValueFactory(new PropertyValueFactory<Balance,String>("amount"));
        BalanceFreeze.setCellValueFactory(new PropertyValueFactory<Balance,String>("freeze"));
        BalanceWithdrawable.setCellValueFactory(new PropertyValueFactory<Balance,String>("withdrawable"));
        BalanceWorthInUSD.setCellValueFactory(new PropertyValueFactory<Balance,String>("worthofusd"));
        ExchangeFunction();


        /*
        int a = bnance.getExchangeStatus();
        System.out.println(a);
        if (a == 0) ExchangeStatus.setText("ok");
        else ExchangeStatus.setText("None");*/
    }
    @FXML
    protected void onBalanceSelected(){
        TotalBalanceInUSD.setText(String.format("%.3f",TB));
    }

    @FXML
    protected void onExchangeChanged(){
        RadioButton selected = (RadioButton) Exchanges.getSelectedToggle();
        Main.CurrentExchange = selected.getText();
        ExchangeFunction();
    }

    @FXML

    protected void onLoginButtonClicked(ActionEvent event) throws IOException {
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


    FXMLLoader loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ControlOrder.fxml")));
    Parent root = loader.load();

    ControlOrder controller = loader.getController();



    Stage pStage = new Stage();
    pStage.setTitle("オーダー編集");
    pStage.setScene(new Scene(root, 600, 400));
    pStage.setResizable(false);
    pStage.initModality(Modality.WINDOW_MODAL);
    pStage.initOwner(LoginButton.getScene().getWindow());
    pStage.show();
    }
    @FXML
    protected void onAddButtonClicked(MouseEvent event) throws IOException{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ControlOrder.fxml")));
        Stage pStage = new Stage();
        pStage.setTitle("オーダー追加");
        pStage.setScene(new Scene(root,600,400));
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