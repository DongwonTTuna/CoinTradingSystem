package com.coinTradingSystem.CoreController.Tables;

import com.coinTradingSystem.Main;
import com.coinTradingSystem.SqlQuery;
import com.coinTradingSystem.CoreController.CoreController;
import com.coinTradingSystem.CoreController.EventHandler.OrderWindowEvent;
import com.coinTradingSystem.UI.MainFrame.TableVariables;
import javafx.collections.ObservableList;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;
import com.coinTradingSystem.CoreController.addEventOnAction;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;


public class OrderTable extends OrderWindowEvent implements addEventOnAction {
    public void InitializeOrderTableView() {
        coreController.controlValue.UpdateOCO();
        coreController.mainFrame.OrderTable.getItems().clear();
        ObservableList<TableVariables.Order> order = coreController.mainFrame.OrderTable.getItems();

        SqlQuery.getOrderList(Main.CurrentExchange).forEach((item) -> {
            System.out.println();
            order.add(new TableVariables.Order(
                    item.get(0),
                    item.get(1),
                    getOrderTypeString(item.get(2)),
                    getCutStringPrice(item.get(3)),
                    getCutStringPrice(item.get(4)),
                    getCutStringAmount(item.get(5)),
                    getCutStringAmount(new BigDecimal(item.get(3)).add(new BigDecimal(item.get(4))).divide(new BigDecimal(2)).multiply(new BigDecimal(item.get(5))).toString())
            ));
        });

        coreController.mainFrame.OrderTable.setRowFactory(tv -> {
            TableRow<TableVariables.Order> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    TableVariables.Order rowData = row.getItem();
                    try {
                        HashMap<String, String> tempHash = new HashMap<>();
                        tempHash.put("uuid", rowData.getUuid());
                        tempHash.put("symbol", rowData.getSymbol());
                        tempHash.put("orderType", rowData.getOrdertype());
                        tempHash.put("targetPrice", rowData.getTargetprice());
                        tempHash.put("triggerPrice", rowData.getTriggerprice());
                        tempHash.put("amount", rowData.getAmount());
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

    private void setCellValue() {
        coreController.mainFrame.OrderUUID.setCellValueFactory(new PropertyValueFactory<TableVariables.Order, String>("uuid"));
        coreController.mainFrame.OrderSymbol.setCellValueFactory(new PropertyValueFactory<TableVariables.Order, String>("symbol"));
        coreController.mainFrame.OrderOrderType.setCellValueFactory(new PropertyValueFactory<TableVariables.Order, String>("ordertype"));
        coreController.mainFrame.OrderTargetPrice.setCellValueFactory(new PropertyValueFactory<TableVariables.Order, String>("targetprice"));
        coreController.mainFrame.OrderTriggerPrice.setCellValueFactory(new PropertyValueFactory<TableVariables.Order, String>("triggerprice"));
        coreController.mainFrame.OrderAmount.setCellValueFactory(new PropertyValueFactory<TableVariables.Order, String>("amount"));
        coreController.mainFrame.OrderWorthInUsd.setCellValueFactory(new PropertyValueFactory<TableVariables.Order,String>("usdworth"));
    }

    public OrderTable(CoreController coreController) {
        this.coreController = coreController;
        onRemoveButtonClicked(coreController.mainFrame.RemoveOne, coreController);
        onRemoveAllButtonClicked(coreController.mainFrame.RemoveAll, coreController);
        onAddOrderButtonClicked(coreController.mainFrame.AddOrder, coreController);
        setCellValue();
    }
}
