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
    exports com.coinTradingSystem.HandleExchange;
    opens com.coinTradingSystem.UI.MainFrame to javafx.fxml;
    exports com.coinTradingSystem.UI.Connect;
    opens com.coinTradingSystem.UI.Connect to javafx.fxml;
    exports com.coinTradingSystem.UI.ControlOrder;
    opens com.coinTradingSystem.UI.ControlOrder to javafx.fxml;
    exports com.coinTradingSystem.UI.MainFrame.CoreController.EventHandler;
    opens com.coinTradingSystem.UI.MainFrame.CoreController.EventHandler to javafx.fxml;
    exports com.coinTradingSystem.UI.MainFrame.CoreController.ExchangeHandler;
    opens com.coinTradingSystem.UI.MainFrame.CoreController.ExchangeHandler to javafx.fxml;
    exports com.coinTradingSystem.UI.MainFrame.CoreController.Variables;
    opens com.coinTradingSystem.UI.MainFrame.CoreController.Variables to javafx.fxml;
    exports com.coinTradingSystem.UI.MainFrame.CoreController;
    opens com.coinTradingSystem.UI.MainFrame.CoreController to javafx.fxml;
    exports com.coinTradingSystem.UI.MainFrame.CoreController.Tables;
    opens com.coinTradingSystem.UI.MainFrame.CoreController.Tables to javafx.fxml;
}