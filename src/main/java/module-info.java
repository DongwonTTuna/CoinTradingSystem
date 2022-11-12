module com.example.cointradingsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.coinTradingSystem to javafx.fxml;
    exports com.coinTradingSystem;
    exports com.coinTradingSystem.UI;
    opens com.coinTradingSystem.UI to javafx.fxml;
}