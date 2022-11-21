package com.coinTradingSystem.UI.Connect;

import com.coinTradingSystem.Main;
import com.coinTradingSystem.SqlQuery;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.util.concurrent.ExecutionException;

public class connectAPI {
    @FXML
    public Button LoginButton;
    @FXML
    public ToggleGroup Exchange;
    @FXML
    public CheckBox toDatabase;
    @FXML
    public TextField API_KEY;
    @FXML
    public TextField SECRET_KEY;

}
