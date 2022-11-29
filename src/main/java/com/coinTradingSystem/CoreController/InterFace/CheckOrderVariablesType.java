package com.coinTradingSystem.CoreController.InterFace;

import java.math.BigDecimal;

public interface CheckOrderVariablesType extends Alert {
    default String getOrderTypeString(String num) {
        switch (num) {
            case "0" -> {
                return "購入";
            }
            case "1" -> {
                return "販売";
            }
            case "2" -> {
                return "損切";
            }
            case "3" -> {
                return "利益補填";
            }
        }
        throw new RuntimeException();
    }

    default short getOrderTypeShort(String num) {
        switch (num) {
            case "購入" -> {
                return 0;
            }
            case "売却" -> {
                return 1;
            }
            case "損切" -> {
                return 2;
            }
            case "利益補填" -> {
                return 3;
            }

        }
        throw new RuntimeException();
    }
    default BigDecimal CheckTypeIsBigDecimal(String str, String msg) {
        try {
            return new BigDecimal(str);
        } catch (Exception e) {
            OpenAlert(msg + "を確認してください。");
        }
        throw new RuntimeException();
    }
}
