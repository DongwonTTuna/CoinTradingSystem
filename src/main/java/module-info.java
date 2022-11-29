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
    exports com.coinTradingSystem.CoreController.Exchange.OrderThread;
    exports com.coinTradingSystem.CoreController;
    opens com.coinTradingSystem.UI.MainFrame to javafx.fxml;
    exports com.coinTradingSystem.UI.Connect;
    opens com.coinTradingSystem.UI.Connect to javafx.fxml;
    exports com.coinTradingSystem.UI.ControlOrder;
    opens com.coinTradingSystem.UI.ControlOrder to javafx.fxml;
    opens com.coinTradingSystem.CoreController to javafx.fxml;
    exports com.coinTradingSystem.UI.MainFrame.Tab;
    opens com.coinTradingSystem.UI.MainFrame.Tab to javafx.fxml;
    exports com.coinTradingSystem.CoreController.InterFace;
    opens com.coinTradingSystem.CoreController.InterFace to javafx.fxml;
    exports com.coinTradingSystem.CoreController.Exchange;
    opens com.coinTradingSystem.CoreController.Exchange to javafx.fxml;
    exports com.coinTradingSystem.UI.MainFrame.Tab.EventHandler;
    opens com.coinTradingSystem.UI.MainFrame.Tab.EventHandler to javafx.fxml;
    exports com.coinTradingSystem.CoreController.Exchange.ExchangeHandler;
    opens com.coinTradingSystem.CoreController.Exchange.ExchangeHandler to javafx.fxml;
    exports com.coinTradingSystem.UI.MainFrame.Tab.EventHandler.OrderWindowFunctions;
    opens com.coinTradingSystem.UI.MainFrame.Tab.EventHandler.OrderWindowFunctions to javafx.fxml;
}