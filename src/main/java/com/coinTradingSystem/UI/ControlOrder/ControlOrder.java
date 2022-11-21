package com.coinTradingSystem.UI.ControlOrder;

import javafx.fxml.FXML;
import javafx.scene.control.*;


public class ControlOrder {
    @FXML
    public Button Submit;
    @FXML
    public Label UUIDLabel;
    @FXML
    public TextField TargetPrice;
    @FXML
    public TextField TriggerPrice;
    @FXML
    public TextField Amount;
    @FXML
    public ChoiceBox<String> SymbolSelectTab;
    @FXML
    public ToggleGroup OrderType;
    @FXML
    public Label ExchangeLabel;
    @FXML
    public RadioButton OrderTypeBuy;
    @FXML
    public RadioButton OrderTypeSell;
    @FXML
    public RadioButton OrderTypeLossCut;
    @FXML
    public RadioButton OrderTypeTakeProfit;

}
