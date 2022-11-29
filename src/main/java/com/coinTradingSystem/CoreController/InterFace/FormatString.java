package com.coinTradingSystem.CoreController.InterFace;

public interface FormatString {
    default String getCutStringPrice(String text){
        double tempDouble = Double.parseDouble(text);
        if (tempDouble == 0.0) return text;
        else if (tempDouble >= 0.1) return String.format("%.3f",tempDouble);
        else if (tempDouble >= 0.01) return String.format("%.4f",tempDouble);
        else if (tempDouble >= 0.001) return String.format("%.5f",tempDouble);
        else if (tempDouble >= 0.0001) return String.format("%.6f",tempDouble);
        else if (tempDouble >= 0.00001) return String.format("%.7f",tempDouble);
        else if (tempDouble >= 0.000001) return String.format("%.8f",tempDouble);
        else if (tempDouble >= 0.0000001) return String.format("%.9f",tempDouble);
        else if (tempDouble >= 0.00000001) return String.format("%.10f",tempDouble);
        else if (tempDouble == 0) return "0";
        throw new RuntimeException();
    }
    default String getCutStringAmount(String text){
        double tempDouble = Double.parseDouble(text);
        if (tempDouble > 1) return String.format("%.0f",tempDouble);
        if (tempDouble >= 0.1) return String.format("%.3f",tempDouble);
        else if (tempDouble >= 0.01) return String.format("%.4f",tempDouble);
        else if (tempDouble >= 0.001) return String.format("%.5f",tempDouble);
        else if (tempDouble >= 0.0001) return String.format("%.6f",tempDouble);
        else if (tempDouble >= 0.00001) return String.format("%.7f",tempDouble);
        else if (tempDouble >= 0.000001) return String.format("%.8f",tempDouble);
        else if (tempDouble >= 0.0000001) return String.format("%.9f",tempDouble);
        else if (tempDouble >= 0.00000001) return String.format("%.10f",tempDouble);
        else if (tempDouble == 0) return "0";
        throw new RuntimeException();
    }
}
