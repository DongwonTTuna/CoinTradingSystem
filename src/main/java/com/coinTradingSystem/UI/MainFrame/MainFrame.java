package com.coinTradingSystem.UI.MainFrame;

import com.coinTradingSystem.CoreController.CoreController;
import javafx.fxml.FXML;


public class MainFrame extends Variables {


    @FXML
    void initialize() {
        coreController = new CoreController(this);
        coreController.controlValue.AddLog("初期化の処理中");

        coreController.exchangeHandler.ExchangeFunction();
    }

}