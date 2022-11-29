package com.coinTradingSystem.UI.MainFrame.Tab;

import com.coinTradingSystem.CoreController.InterFace.CheckOrderVariablesType;
import com.coinTradingSystem.Main;
import com.coinTradingSystem.UI.MainFrame.Tab.EventHandler.OrderTabEventHandler;


public class OrderTab extends OrderTabEventHandler implements CheckOrderVariablesType {
    public OrderTab() {
        onRemoveButtonClicked(Main.mainFrame.RemoveOne);
        onRemoveAllButtonClicked(Main.mainFrame.RemoveAll);
        onAddOrderButtonClicked(Main.mainFrame.AddOrder);
    }
}
