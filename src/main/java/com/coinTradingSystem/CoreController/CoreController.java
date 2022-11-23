package com.coinTradingSystem.CoreController;

import com.coinTradingSystem.CoreController.EventHandler.CallBackFunctions;
import com.coinTradingSystem.CoreController.EventHandler.EventHandler;
import com.coinTradingSystem.CoreController.ExchangeHandler.ExchangeHandler;
import com.coinTradingSystem.CoreController.Tables.BalanceTable;
import com.coinTradingSystem.CoreController.Tables.OrderTable;
import com.coinTradingSystem.CoreController.TradeSystem.TradeCore;
import com.coinTradingSystem.CoreController.Variables.ControlValue;
import com.coinTradingSystem.UI.MainFrame.MainFrame;

public class CoreController {

    public MainFrame mainFrame;
    public CallBackFunctions callBackFunctions;
    public ExchangeHandler exchangeHandler;
    public BalanceTable balanceTable;
    public OrderTable orderTable;
    public ControlValue controlValue;
    public EventHandler eventHandler;

    public TradeCore tradeCore;

    public CoreController(MainFrame mainFrame){
        this.mainFrame = mainFrame;
        this.callBackFunctions = new CallBackFunctions(this);
        this.exchangeHandler = new ExchangeHandler(this);
        this.orderTable = new OrderTable(this);
        this.balanceTable = new BalanceTable(this);
        this.controlValue = new ControlValue(this);
        this.eventHandler = new EventHandler(this);
        this.tradeCore = new TradeCore(this);
    }


}
