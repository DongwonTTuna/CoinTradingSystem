package com.coinTradingSystem.UI.MainFrame.Tab.EventHandler;

import com.coinTradingSystem.CoreController.InterFace.AddLog;
import com.coinTradingSystem.CoreController.InterFace.CheckOrderVariablesType;
import com.coinTradingSystem.CoreController.InterFace.FormatString;
import com.coinTradingSystem.Main;
import com.coinTradingSystem.SqlQuery;
import com.coinTradingSystem.UI.MainFrame.MainFrame;
import com.coinTradingSystem.UI.MainFrame.Tab.EventHandler.OrderWindowFunctions.OpenOrderWindowHandler;
import com.coinTradingSystem.UI.MainFrame.TableVariables;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableRow;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Objects;

public class OrderTabEventHandler extends OpenOrderWindowHandler implements CheckOrderVariablesType, FormatString, AddLog {
    public MainFrame mainFrame;

    public void InitializeOrderTableView() {
        mainFrame.OrderTable.getItems().clear();
        ObservableList<TableVariables.Order> order = mainFrame.OrderTable.getItems();

        SqlQuery.getOrderList(Main.Exchange.getExchangeName()).forEach((item) -> {
            String worth = getCutStringAmount(new BigDecimal(item.get(3)).add(new BigDecimal(item.get(4))).divide(new BigDecimal(2)).multiply(new BigDecimal(item.get(5))).toString());
            order.add(new TableVariables.Order(
                    item.get(0),
                    item.get(1),
                    getOrderTypeString(item.get(2)),
                    getCutStringPrice(item.get(3)),
                    getCutStringPrice(item.get(4)),
                    getCutStringAmount(item.get(5)),
                    Objects.equals(Main.Exchange.getExchangeName(), "UPBIT") ? "₩ " + worth : "$ " + worth
            ));
        });

        mainFrame.OrderTable.setRowFactory(tv -> {
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
                        OpenOrderWindowEvent(tempHash);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            return row;
        });
        mainFrame.OrderTable.setItems(order);
    }

    public void onRemoveButtonClicked(Button btn) {
        btn.setOnAction(actionEvent -> {
                TableVariables.Order order = mainFrame.OrderTable.getSelectionModel().getSelectedItem();
                if (order == null) return;
                String uuid = order.getUuid();
                SqlQuery.removeOrder(uuid);
                AddLog("オーダーが削除されました");
        });
    }

    public void onRemoveAllButtonClicked(Button btn) {
        btn.setOnAction(actionEvent ->{
                SqlQuery.removeAllOrders();
                InitializeOrderTableView();
                AddLog("全てのオーダーが削除されました");
        });
    }

    public void onAddOrderButtonClicked(Button btn) {
        btn.setOnAction(actionEvent -> {
                try {
                    OpenOrderWindowEvent(null);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        });
    }

}
