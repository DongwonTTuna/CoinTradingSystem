package com.coinTradingSystem;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;





public class Main extends Application{

    public static String CurrentExchange;

    private static HashMap<String,HashMap<String,String>> API;
    @Override
    public void start(Stage pStage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/MainFrame.fxml")));

        pStage.setTitle("仮想通貨取引プログラム");
        pStage.setScene(new Scene(root,1400,700));
        pStage.setResizable(false);
        pStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
        pStage.show();
    }

    public static void setAPI(String exchange, String apikey, String secretkey){
        CurrentExchange = exchange;
        HashMap<String,String> tempHashmap = new HashMap<>();
        tempHashmap.put("apikey",apikey);
        tempHashmap.put("secretkey",secretkey);
        API.put(exchange,tempHashmap);
    }
    public static void getAPIfromDatabase(){
        API = SqlQuery.getAPI();
    }

    public static HashMap<String,String> getOneAPI(String exchange){
        return API.get(exchange);
    }
    public static void main(String[] args) {
        getAPIfromDatabase();
        CurrentExchange = "BINANCE";
        launch();
        //sql.insertOrder("BINANCE", (short)0, "BTCUSDT", 0, 20000.0, 12.0);
        //sql.updateOrder("4bafb6ff-b26a-48dd-b114-85abe9371e47", "FTX", (short)0, "ETHUSD", 0, 0, 0);
        //System.out.println(sql.getOrderList("BINANCE"));
    }
}
