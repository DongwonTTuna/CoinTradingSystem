package com.coinTradingSystem;
import com.coinTradingSystem.CoreController.CoreController;
import com.coinTradingSystem.CoreController.Exchange.ExchangeMain;
import com.coinTradingSystem.CoreController.InterFace.AddLog;
import com.coinTradingSystem.UI.MainFrame.MainFrame;
import com.coinTradingSystem.UI.MainFrame.Tab.UpdateTab;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;





public class Main extends Application implements AddLog{
    public static volatile CoreController coreController;
    public static volatile MainFrame mainFrame;

    public static volatile ExchangeMain Exchange;
    public static volatile String CurrentTabText;
    public static volatile boolean isExchangeLoaded = false;
    private static volatile HashMap<String,HashMap<String,String>> API;
    @Override
    public void start(Stage pStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/MainFrame.fxml")));
        Parent root = loader.load();
        mainFrame = loader.getController();

        AddLog("初期化の処理中");
        mainFrame.updateTab = new UpdateTab();
        new CoreController();


        pStage.setTitle("仮想通貨取引プログラム");
        pStage.setScene(new Scene(root,1400,700));
        pStage.setResizable(false);
        pStage.setOnCloseRequest(windowEvent ->  {
                Platform.exit();
                System.exit(0);
        });
        pStage.show();
    }

    public static void setAPI(String exchange, String apikey, String secretkey){
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
        launch();
    }
}
