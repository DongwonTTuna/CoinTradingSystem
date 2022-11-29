package com.coinTradingSystem.CoreController.InterFace;

import com.coinTradingSystem.Main;
import com.coinTradingSystem.UI.MainFrame.MainFrame;
import javafx.application.Platform;
import javafx.scene.control.Label;

public interface AddLog {
    default void AddLog(String msg){
        Platform.runLater(()-> Main.mainFrame.LogBox.getChildren().add(new Label(msg)));
    }
}
