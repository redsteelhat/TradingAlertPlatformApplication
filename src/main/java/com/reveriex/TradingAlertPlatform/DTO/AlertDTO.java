package com.reveriex.TradingAlertPlatform.DTO;

public class AlertDTO {
    private String symbol;
    private double price;
    private String side;

    // Getters ve Setters
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }
}
