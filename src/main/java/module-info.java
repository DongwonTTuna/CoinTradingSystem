module com.example.cointradingsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.json;
    requires commons.codec;
    requires okhttp3;
    requires awaitility;
    requires xchange.binance;
    requires xchange.gateio;
    requires xchange.upbit;
    requires xchange.core;

    opens com.coinTradingSystem to javafx.fxml;
    exports com.coinTradingSystem;
    exports com.coinTradingSystem.UI.MainFrame;
    exports com.coinTradingSystem.ExchangeClass;
    exports com.coinTradingSystem.CoreController.TradeSystem;
    opens com.coinTradingSystem.UI.MainFrame to javafx.fxml;
    exports com.coinTradingSystem.UI.Connect;
    opens com.coinTradingSystem.UI.Connect to javafx.fxml;
    exports com.coinTradingSystem.UI.ControlOrder;
    opens com.coinTradingSystem.UI.ControlOrder to javafx.fxml;
    exports com.coinTradingSystem.CoreController.EventHandler;
    opens com.coinTradingSystem.CoreController.EventHandler to javafx.fxml;
    exports com.coinTradingSystem.CoreController.ExchangeHandler;
    opens com.coinTradingSystem.CoreController.ExchangeHandler to javafx.fxml;
    exports com.coinTradingSystem.CoreController.Variables;
    opens com.coinTradingSystem.CoreController.Variables to javafx.fxml;
    exports com.coinTradingSystem.CoreController;
    opens com.coinTradingSystem.CoreController to javafx.fxml;
    exports com.coinTradingSystem.CoreController.Tables;
    opens com.coinTradingSystem.CoreController.Tables to javafx.fxml;
}