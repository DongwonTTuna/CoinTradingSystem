package com.coinTradingSystem.CoreController.InterFace;

public interface Alert {
    default void OpenAlert(String contentText) {
        javafx.scene.control.Alert alrt = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
        alrt.setTitle("エアー");
        alrt.setHeaderText("エアーが発生しました");
        alrt.setContentText(contentText);
        alrt.showAndWait();
    }
}
