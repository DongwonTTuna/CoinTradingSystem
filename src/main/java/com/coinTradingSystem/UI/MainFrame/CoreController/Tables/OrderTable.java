package com.coinTradingSystem.UI.MainFrame.CoreController.Tables;

import com.coinTradingSystem.Main;
import com.coinTradingSystem.SqlQuery;
import com.coinTradingSystem.UI.MainFrame.CoreController.CoreController;
import com.coinTradingSystem.UI.MainFrame.CoreController.EventHandler.OrderWindowEvent;
import com.coinTradingSystem.UI.MainFrame.TableVariables;
import javafx.collections.ObservableList;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;
import com.coinTradingSystem.UI.MainFrame.CoreController.addEventOnAction;

import java.io.IOException;
import java.util.HashMap;


public class OrderTable extends OrderWindowEvent implements addEventOnAction{
    public void InitializeOrderTableView(){
        coreController.controlValue.UpdateOCO();
        coreController.mainFrame.OrderTable.getItems().clear();
        ObservableList<TableVariables.Order> order = coreController.mainFrame.OrderTable.getItems();

        SqlQuery.getOrderList(Main.CurrentExchange).forEach((item)->{
            order.add(new TableVariables.Order(
                    item.get(0),
                    item.get(1),
                    getOrderTypeString(item.get(2)),
                    item.get(3),
                    item.get(4),
                    item.get(5)
            ));
        });

        coreController.mainFrame.OrderTable.setRowFactory( tv -> {
            TableRow<TableVariables.Order> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    TableVariables.Order rowData = row.getItem();
                    try {
                        HashMap<String,String> tempHash = new HashMap<>();
                        tempHash.put("uuid",rowData.getUuid());
                        tempHash.put("symbol", rowData.getSymbol());
                        tempHash.put("orderType",rowData.getOrdertype());
                        tempHash.put("targetPrice",rowData.getTargetprice());
                        tempHash.put("triggerPrice",rowData.getTriggerprice());
                        tempHash.put("amount",rowData.getAmount());
                        super.OpenOrderWindowEvent(tempHash);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            return row;
        });
        coreController.mainFrame.OrderTable.setItems(order);
    }

    private void setCellValue(){
        coreController.mainFrame.OrderUUID.setCellValueFactory(new PropertyValueFactory<TableVariables.Order,String>("uuid"));
        coreController.mainFrame.OrderSymbol.setCellValueFactory(new PropertyValueFactory<TableVariables.Order,String>("symbol"));
        coreController.mainFrame.OrderOrderType.setCellValueFactory(new PropertyValueFactory<TableVariables.Order,String>("ordertype"));
        coreController.mainFrame.OrderTargetPrice.setCellValueFactory(new PropertyValueFactory<TableVariables.Order,String>("targetprice"));
        coreController.mainFrame.OrderTriggerPrice.setCellValueFactory(new PropertyValueFactory<TableVariables.Order,String>("triggerprice"));
        coreController.mainFrame.OrderAmount.setCellValueFactory(new PropertyValueFactory<TableVariables.Order,String>("amount"));
    }

    public OrderTable(CoreController coreController){
        this.coreController = coreController;
        onRemoveButtonClicked(coreController.mainFrame.RemoveOne,coreController);
        onRemoveAllButtonClicked(coreController.mainFrame.RemoveAll,coreController);
        onAddOrderButtonClicked(coreController.mainFrame.AddOrder,coreController);
        setCellValue();
    }
}
