package com.coinTradingSystem.UI.MainFrame;

import com.coinTradingSystem.CoreController.CoreController;
import com.coinTradingSystem.UI.MainFrame.Tab.UpdateTab;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class Variables extends TableVariables {

    @FXML
    public Label ExchangeStatus;
    @FXML
    public Label TotalBalance;
    @FXML
    public Label OpenOrders;
    @FXML
    public Label TotalTrades;
    @FXML
    public Label TodayProfit;
    @FXML
    public ToggleGroup Exchanges;
    @FXML
    public Button LoginButton;
    @FXML
    public Button RemoveOne;
    @FXML
    public Button AddOrder;
    @FXML
    public Button RemoveAll;
    @FXML
    public VBox LogBox;
    @FXML
    public Label TotalBalanceInUSD;
    @FXML
    public TextField ConnectionLog;
    @FXML
    public SplitPane Splitpane;
    @FXML
    public RadioButton ExchangeB;
    @FXML
    public RadioButton ExchangeG;
    @FXML
    public RadioButton ExchangeM;
    @FXML
    public TabPane InfoTab;
    @FXML
    public Tab StatusTab;
    @FXML
    public Tab BalanceTab;
    @FXML
    public Tab OrderTab;

    public UpdateTab updateTab;
    public double TB = 0.0;

}
