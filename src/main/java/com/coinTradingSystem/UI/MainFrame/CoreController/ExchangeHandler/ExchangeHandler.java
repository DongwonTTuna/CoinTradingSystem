package com.coinTradingSystem.UI.MainFrame.CoreController.ExchangeHandler;


import com.coinTradingSystem.HandleExchange.Exchanges;
import com.coinTradingSystem.Main;
import com.coinTradingSystem.UI.MainFrame.CoreController.CoreController;
import org.knowm.xchange.binance.BinanceExchange;
import org.knowm.xchange.gateio.GateioExchange;
import org.knowm.xchange.upbit.UpbitExchange;

public class ExchangeHandler extends ExchangeTabFunctions {

    private void ExchangeSetup() {
        coreController.mainFrame.ConnectionLog.setText(Main.CurrentExchange + "に接続中");
        switch (Main.CurrentExchange) {
            case "BINANCE" -> exchange = new Exchanges(BinanceExchange.class.getName(),coreController);
            case "GATEIO" -> exchange = new Exchanges(GateioExchange.class.getName(),coreController);
            case "UPBIT" -> exchange = new Exchanges(UpbitExchange.class.getName(),coreController);
        }
    }

    public void ExchangeFunction() {
        coreController.controlValue.InitializeAllValue();
        ExchangeSetup();
        coreController.controlValue.AddLog("初期化が完了しました");
    }

    public ExchangeHandler(CoreController coreController) {
        super.coreController = coreController;
    }
}
