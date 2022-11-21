package com.coinTradingSystem.UI.MainFrame.CoreController.Tables;

import com.coinTradingSystem.UI.MainFrame.CoreController.CoreController;
import com.coinTradingSystem.UI.MainFrame.TableVariables;
import javafx.scene.control.cell.PropertyValueFactory;

public class BalanceTable {
    private final CoreController coreController;


    private void setCellValue(){
        coreController.mainFrame.BalanceSymbol.setCellValueFactory(new PropertyValueFactory<TableVariables.Balance,String>("symbol"));
        coreController.mainFrame.BalanceAmount.setCellValueFactory(new PropertyValueFactory<TableVariables.Balance,String>("amount"));
        coreController.mainFrame.BalanceFreeze.setCellValueFactory(new PropertyValueFactory<TableVariables.Balance,String>("freeze"));
        coreController.mainFrame.BalanceWithdrawable.setCellValueFactory(new PropertyValueFactory<TableVariables.Balance,String>("withdrawable"));
        coreController.mainFrame.BalanceWorthInUSD.setCellValueFactory(new PropertyValueFactory<TableVariables.Balance,String>("worthofusd"));
    }
    public BalanceTable(CoreController coreController){
        this.coreController = coreController;
        setCellValue();
    }
}
