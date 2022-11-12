package com.coinTradingSystem;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.ArrayList;

public class SqlQuery {
    
    private final String connurl = "jdbc:postgresql://localhost:5432/TRADINGBOT";
    private final String user = "postgres";
    private final String password = "0790";

    private ArrayList<ArrayList<String>> getOrderQuery(String exchange){
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(connurl,user,password);){
            PreparedStatement st = connection.prepareStatement("SELECT * FROM ORDERS WHERE exchange = ?");
            st.setString(1, exchange);
            ResultSet rs = st.executeQuery();
            while( rs.next() ){
                ArrayList<String> tempList = new ArrayList<>();
                tempList.add(rs.getString("uuid")); 
                tempList.add(rs.getString("exchange"));
                tempList.add("" + rs.getInt("ordertype"));
                tempList.add(rs.getString("symbol"));
                tempList.add("" + rs.getDouble("checkprice"));
                tempList.add("" + rs.getDouble("targetprice"));
                tempList.add("" + rs.getDouble("amount"));
                result.add(tempList);
            }
            rs.close();
            st.close();
            
        } catch( SQLException e){
            e.printStackTrace();
        }
        return result;
    }


    private int updateOrderQuery(String uuid, String exchange, short ordertype, String symbol, double checkprice, double targetprice, double amount){
        try(Connection connection = DriverManager.getConnection(connurl, user, password)){
            PreparedStatement st = connection.prepareStatement("UPDATE ORDERS SET (exchange,ordertype,symbol,checkprice,targetprice,amount) = (?,?,?,?,?,?) WHERE uuid = ?");
            st.setString(1, exchange);
            st.setShort(2, ordertype);
            st.setString(3, symbol);
            st.setDouble(4,checkprice);
            st.setDouble(5, targetprice);
            st.setDouble(6, amount);
            st.setString(7, uuid);
            st.executeUpdate();
            st.close();
        } catch( SQLException e){
            e.printStackTrace();
            return 1;
        }

        return 0;
    }


    private int deleteOrderQuery(String uuid){
        try(Connection connection = DriverManager.getConnection(connurl, user, password)){
            PreparedStatement st = connection.prepareStatement("DELETE FROM ORDERS WHERE uuid = ?");
            st.setString(1,uuid);
            st.executeUpdate();
            st.close();
        } catch( SQLException e){
            e.printStackTrace();
            return 1;
        }


        return 0;
    }

    private int insertOrderQuery(String exchange, short ordertype, String symbol, double checkprice, double targetprice, double amount){
        try(Connection connection = DriverManager.getConnection(connurl, user, password)){
            PreparedStatement st = connection.prepareStatement("INSERT INTO ORDERS (uuid,exchange,ordertype,symbol,checkprice,targetprice,amount) VALUES (?,?,?,?,?,?,?)");
            String uuid = UUID.randomUUID().toString();
            st.setString(1,uuid);
            st.setString(2, exchange);
            st.setShort(3, ordertype);
            st.setString(4, symbol);
            st.setDouble(5,checkprice);
            st.setDouble(6, targetprice);
            st.setDouble(7, amount);
            st.executeUpdate();
            st.close();
        } catch( SQLException e){
            e.printStackTrace();
            return 1;
        }

        return 0;
    }

    private int updateAPIQuery(String exchange, String api_key, String secret_key){
        try(Connection connection = DriverManager.getConnection(connurl, user, password)){
            PreparedStatement st = connection.prepareStatement("UPDATE APIKEYS SET (api_key,secret_key) = (?,?) WHERE exchange = ?");
            String uuid = UUID.randomUUID().toString();
            st.setString(1,uuid);
            st.executeUpdate();
            st.close();
        } catch( SQLException e) {
            e.printStackTrace();
            return 1;
        }
        return 0;
    }

    public ArrayList<ArrayList<String>> getOrderList(String exchange){
        return getOrderQuery(exchange);
    }

    public int updateOrder(String uuid, String exchange, short ordertype, String symbol, double checkprice, double targetprice, double amount){
        return updateOrderQuery(uuid, exchange, ordertype, symbol, checkprice, targetprice, amount);
    }

    public int deleteOrder(String uuid){
        return deleteOrderQuery(uuid);
    }

    public int insertOrder(String exchange, short ordertype, String symbol, double checkprice, double targetprice, double amount){
        return insertOrderQuery(exchange, ordertype, symbol, checkprice, targetprice, amount);
    }

    public int updateAPI(String exchange, String api_key, String secret_key){
        return updateAPIQuery(exchange,api_key,secret_key);
    }

}
