package com.coinTradingSystem.UI;

import com.coinTradingSystem.Main;
import com.coinTradingSystem.SqlQuery;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.util.concurrent.ExecutionException;

public class connectAPI {
    @FXML
    Button LoginButton;
    @FXML
    ToggleGroup Exchange;
    @FXML
    CheckBox toDatabase;
    @FXML
    TextField API_KEY;
    @FXML
    TextField SECRET_KEY;

}
