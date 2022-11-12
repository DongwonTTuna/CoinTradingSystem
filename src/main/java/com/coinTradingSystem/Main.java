package com.coinTradingSystem;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;





public class Main extends Application{
    @Override
    public void start(Stage pStage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/MainFrame.fxml")));
        pStage.setTitle("仮想通貨取引プログラム");
        pStage.setScene(new Scene(root,1400,800));
        pStage.setResizable(false);
        pStage.show();
    }

    public static void main(String[] args) {
        SqlQuery sql = new SqlQuery();
        //sql.insertOrder("BINANCE", (short)0, "BTCUSDT", 0, 20000.0, 12.0);
        //sql.updateOrder("4bafb6ff-b26a-48dd-b114-85abe9371e47", "FTX", (short)0, "ETHUSD", 0, 0, 0);
        //System.out.println(sql.getOrderList("BINANCE"));
        launch();
    }
}
