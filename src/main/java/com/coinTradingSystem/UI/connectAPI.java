package com.coinTradingSystem.UI;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;

public class connectAPI {

    @FXML
    private ToggleGroup Exchange;
    @FXML
    private CheckBox toDatabase;
    @FXML
    private TextField API_KEY;
    @FXML
    private TextField SECRET_KEY;

    @FXML
    protected void onSaveButtonClicked(MouseEvent event){
        RadioButton SelectedRB = (RadioButton)Exchange.getSelectedToggle();
        System.out.println(SelectedRB.getText());
        System.out.println(toDatabase.isSelected());
        System.out.println(API_KEY.getText());
        System.out.println(SECRET_KEY.getText());
    }
}
