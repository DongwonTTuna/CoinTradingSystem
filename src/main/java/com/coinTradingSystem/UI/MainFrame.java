package com.coinTradingSystem.UI;

import com.coinTradingSystem.SqlQuery;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainFrame {
    @FXML
    private Text ExchangeStatus;
    @FXML
    private Text TotalBalance;
    @FXML
    private Text TradeInLast24;
    @FXML
    private Text TotalProfit;
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
    private TextFlow LogText;

    private SqlQuery sql;
    @FXML

    protected void onLoginButtonClicked(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/LoginToApi.fxml")));
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