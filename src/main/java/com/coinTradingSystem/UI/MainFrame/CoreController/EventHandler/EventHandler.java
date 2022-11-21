package com.coinTradingSystem.UI.MainFrame.CoreController.EventHandler;

import com.coinTradingSystem.Main;
import com.coinTradingSystem.SqlQuery;
import com.coinTradingSystem.UI.Connect.connectAPI;
import com.coinTradingSystem.UI.MainFrame.CoreController.CoreController;
import com.coinTradingSystem.UI.MainFrame.CoreController.addEventOnAction;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class EventHandler implements addEventOnAction {
    private final CoreController coreController;

    protected void onBalanceSelected(){
        coreController.mainFrame.TotalBalanceInUSD.setText(String.format("%.3f",coreController.mainFrame.TB));
    }



    public void onLoginButtonClicked() throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/LoginToApi.fxml")));
        Parent root = loader.load();

        connectAPI login = loader.getController();
        login.LoginButton.setOnAction(actionEvent -> {
            RadioButton SelectedRB = (RadioButton)login.Exchange.getSelectedToggle();
            String exchange =  SelectedRB.getText();
            String api_key = login.API_KEY.getText();
            String secret_key = login.SECRET_KEY.getText();
            if (!Main.isExchangeLoaded) {
                Main.setAPI(exchange, api_key, secret_key);
                coreController.exchangeHandler.ExchangeFunction();
            }
            if (login.toDatabase.isSelected()){
                SqlQuery.updateAPI(exchange,api_key,secret_key);
            }
            login.API_KEY.setText("");
            login.SECRET_KEY.setText("");
        });



        Stage pStage = new Stage();
        pStage.setTitle("API 接続 Window");
        pStage.setScene(new Scene(root,600,400));
        pStage.setResizable(false);
        pStage.initModality(Modality.WINDOW_MODAL);
        pStage.initOwner(coreController.mainFrame.LoginButton.getScene().getWindow());

        pStage.show();
    }


    private void addEventHandlerOnInitial(){
        OnExchangeChanged(coreController.mainFrame.ExchangeB,coreController);
        OnExchangeChanged(coreController.mainFrame.ExchangeG, coreController);
        OnExchangeChanged(coreController.mainFrame.ExchangeM, coreController);
        AddLoginEvent(coreController.mainFrame.LoginButton,coreController);
        OnTabChanged(coreController.mainFrame.BalanceTab,coreController);
        OnTabChanged(coreController.mainFrame.OrderTab,coreController);
        OnTabChanged(coreController.mainFrame.StatusTab,coreController);
        OnDividerMove(coreController.mainFrame.Splitpane);
    }




    public EventHandler(CoreController coreController){
        this.coreController = coreController;
        addEventHandlerOnInitial();
        coreController.controlValue.InitializeOnStart();
    }
}
