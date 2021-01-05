package com.bibi.entity;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/5/8
 */
public class Pankou {
    private Double amount;
    private String amountStr;
    private Double price;
    private String priceStr;
    private int position;

    public Pankou(int position,String priceStr,String amountStr){
        this.position = position;
        this.priceStr = priceStr;
        this.amountStr = amountStr;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getAmountStr() {
        return amountStr;
    }

    public void setAmountStr(String amountStr) {
        this.amountStr = amountStr;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPriceStr() {
        return priceStr;
    }

    public void setPriceStr(String priceStr) {
        this.priceStr = priceStr;
    }
}
