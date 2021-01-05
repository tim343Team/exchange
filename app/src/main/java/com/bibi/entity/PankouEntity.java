package com.bibi.entity;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/5/8
 */
public class PankouEntity {
    private TextItems buyTradePlate;
    private TextItems sellTradePlate;
    private String symbol;
    private Double price;
    private Double cny;

    public TextItems getBuyTradePlate() {
        return buyTradePlate;
    }

    public void setBuyTradePlate(TextItems buyTradePlate) {
        this.buyTradePlate = buyTradePlate;
    }

    public TextItems getSellTradePlate() {
        return sellTradePlate;
    }

    public void setSellTradePlate(TextItems sellTradePlate) {
        this.sellTradePlate = sellTradePlate;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getCny() {
        return cny;
    }

    public void setCny(Double cny) {
        this.cny = cny;
    }
}
