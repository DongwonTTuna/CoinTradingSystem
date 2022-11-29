package com.coinTradingSystem.UI.Connect;

import com.coinTradingSystem.Main;
import com.coinTradingSystem.SqlQuery;
import com.coinTradingSystem.UI.Connect.EventHandler.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.util.concurrent.ExecutionException;

public class connectAPI extends EventHandler {


    @FXML
    public void initialize(){
        addEventHandler();
    }

}
