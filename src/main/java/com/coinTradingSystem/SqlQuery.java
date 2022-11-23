package com.coinTradingSystem;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.UUID;
import java.util.ArrayList;

public class SqlQuery {

    private static final String connurl = "jdbc:postgresql://localhost:5432/TRADINGBOT";
    private static final String user = "postgres";
    private static final String password = "0790";

    private static ArrayList<ArrayList<String>> getOrderQuery(String exchange) {
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(connurl, user, password);) {
            PreparedStatement st = connection.prepareStatement("SELECT * FROM ORDERS WHERE exchange = ?");
            st.setString(1, exchange);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                ArrayList<String> tempList = new ArrayList<>();
                tempList.add(rs.getString("uuid"));
                tempList.add(rs.getString("symbol"));
                tempList.add("" + rs.getShort("ordertype"));
                tempList.add("" + rs.getDouble("targetprice"));
                tempList.add("" + rs.getDouble("checkprice"));
                tempList.add("" + rs.getDouble("amount"));
                result.add(tempList);
            }
            rs.close();
            st.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    private static int updateOrderQuery(String uuid, String exchange, short ordertype, String symbol, BigDecimal triggerPrice, BigDecimal targetprice, BigDecimal amount) {
        try (Connection connection = DriverManager.getConnection(connurl, user, password)) {
            PreparedStatement st = connection.prepareStatement("UPDATE ORDERS SET (exchange,ordertype,symbol,checkprice,targetprice,amount) = (?,?,?,?,?,?) WHERE uuid = ?");
            st.setString(1, exchange);
            st.setShort(2, ordertype);
            st.setString(3, symbol);
            st.setBigDecimal(4, triggerPrice);
            st.setBigDecimal(5, targetprice);
            st.setBigDecimal(6, amount);
            st.setString(7, uuid);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }

        return 0;
    }


    private static int removeOrderQuery(String uuid) {
        try (Connection connection = DriverManager.getConnection(connurl, user, password)) {
            PreparedStatement st = connection.prepareStatement("DELETE FROM ORDERS WHERE uuid = ?");
            st.setString(1, uuid);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }


        return 0;
    }

    private static void removeAllOrderQuery() {
        try (Connection connection = DriverManager.getConnection(connurl, user, password)) {
            PreparedStatement st = connection.prepareStatement("DELETE FROM ORDERS");
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int insertOrderQuery(String exchange, short ordertype, String symbol, BigDecimal triggerPrice, BigDecimal targetprice, BigDecimal amount) {
        try (Connection connection = DriverManager.getConnection(connurl, user, password)) {
            PreparedStatement st = connection.prepareStatement("INSERT INTO ORDERS (uuid,exchange,ordertype,symbol,checkprice,targetprice,amount) VALUES (?,?,?,?,?,?,?)");
            String uuid = UUID.randomUUID().toString();
            st.setString(1, uuid);
            st.setString(2, exchange);
            st.setShort(3, ordertype);
            st.setString(4, symbol);
            st.setBigDecimal(5, triggerPrice);
            st.setBigDecimal(6, targetprice);
            st.setBigDecimal(7, amount);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }

        return 0;
    }

    private static int updateAPIQuery(String exchange, String api_key, String secret_key) {
        try (Connection connection = DriverManager.getConnection(connurl, user, password)) {
            PreparedStatement st = connection.prepareStatement("UPDATE APIKEYS SET (api_key,secret_key) = (?,?) WHERE exchange = ?");
            st.setString(1, api_key);
            st.setString(2, secret_key);
            st.setString(3, exchange);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return 1;
        }
        return 0;
    }

    private static HashMap<String, HashMap<String, String>> getAPIQuery() {
        HashMap<String, HashMap<String, String>> result = new HashMap<>();
        try (Connection connection = DriverManager.getConnection(connurl, user, password)) {
            PreparedStatement st = connection.prepareStatement("SELECT * FROM APIKEYS");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                HashMap<String, String> tempHashMap = new HashMap<>();
                tempHashMap.put("apikey", rs.getString("api_key"));
                tempHashMap.put("secretkey", rs.getString("secret_key"));
                result.put(rs.getString("exchange"), tempHashMap);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static int getTradesNumQuery(String exchange) {
        try (Connection connection = DriverManager.getConnection(connurl, user, password)) {
            PreparedStatement st = connection.prepareStatement("SELECT num FROM TotalTradesNPnL where exchange = ?");
            st.setString(1, exchange);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                return rs.getInt("num");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    private static void increaseTradesNumQuery(String exchange) {
        try (Connection connection = DriverManager.getConnection(connurl, user, password)) {
            PreparedStatement st = connection.prepareStatement("UPDATE TotalTradesNPnL SET num = num + 1 WHERE exchange = ?");
            st.setString(1, exchange);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updatePBalQuery(String exchange, double num) {
        try (Connection connection = DriverManager.getConnection(connurl, user, password)) {
            PreparedStatement st = connection.prepareStatement("UPDATE TotalTradesNPnL SET Pbal = ? WHERE exchange = ?");
            st.setDouble(1, num);
            st.setString(2, exchange);
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static double getPBalQuery(String exchange) {
        try (Connection connection = DriverManager.getConnection(connurl, user, password)) {

            PreparedStatement st = connection.prepareStatement("SELECT Pbal FROM TotalTradesNPnL where exchange = ?");
            st.setString(1, exchange);
            ResultSet rs = st.executeQuery();

            rs.next();
            return rs.getDouble("Pbal");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    private static BigDecimal getLastProfitQuery(String exchange) {
        try (Connection connection = DriverManager.getConnection(connurl, user, password)) {

            PreparedStatement st = connection.prepareStatement("SELECT balance FROM B." + exchange + "PROFIT ORDER BY day DESC LIMIT 1");
            ResultSet rs = st.executeQuery();

            rs.next();
            if (rs.getFetchSize() == 0) return new BigDecimal(-1000);
            return rs.getBigDecimal("balance");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    private static void insertProfitQuery(String exchange, BigDecimal balance) {
        try (Connection connection = DriverManager.getConnection(connurl, user, password)) {

            PreparedStatement st = connection.prepareStatement("INSERT INTO FROM B." + exchange + "PROFIT VALUES (day,balance) = (?,?)");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String today = LocalDate.now().format(formatter);
            st.setInt(1, Integer.parseInt(today));
            st.setBigDecimal(2, balance);

            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static ArrayList<ArrayList<String>> getOrderList(String exchange) {
        return getOrderQuery(exchange);
    }

    public static int updateOrder(String uuid, String exchange, short ordertype, String symbol, BigDecimal triggerPrice, BigDecimal targetprice, BigDecimal amount) {
        return updateOrderQuery(uuid, exchange, ordertype, symbol, triggerPrice, targetprice, amount);
    }

    public static int removeOrder(String uuid) {
        return removeOrderQuery(uuid);
    }

    public static int addOrder(String exchange, short ordertype, String symbol, BigDecimal triggerPrice, BigDecimal targetprice, BigDecimal amount) {
        return insertOrderQuery(exchange, ordertype, symbol, triggerPrice, targetprice, amount);
    }

    public static int updateAPI(String exchange, String api_key, String secret_key) {
        return updateAPIQuery(exchange, api_key, secret_key);
    }

    public static HashMap<String, HashMap<String, String>> getAPI() {
        return getAPIQuery();
    }

    public static int getTradesNum(String exchange) {
        return getTradesNumQuery(exchange);
    }

    public static void increaseTradesNum(String exchange) {
        increaseTradesNumQuery(exchange);
    }

    public static void updatePBal(String exchange, double num) {
        updatePBalQuery(exchange, num);
    }

    public static void removeAllOrders() {
        removeAllOrderQuery();
    }

    public static double getPbal(String exchange) {
        return getPBalQuery(exchange);
    }

    public static BigDecimal getLastProfit(String exchange) {
        return getLastProfitQuery(exchange);
    }

}
