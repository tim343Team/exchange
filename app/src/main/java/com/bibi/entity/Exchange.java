package com.bibi.entity;

/**
 * Created by Administrator on 2018/1/30.
 */

public class Exchange {
    private String idText;
    private int type;// 1 买入记录 2 卖出记录\
    private String priceStr;
    private Double amount;
    private Double price;
    private String amountStr;
    private String totalAmount;
    private int position;


    public Exchange(int position,String priceStr,String amountStr){
        this.position = position;
        this.priceStr = priceStr;
        this.amountStr = amountStr;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getAmountStr() {
        return amountStr;
    }

    public void setAmountStr(String amountStr) {
        this.amountStr = amountStr;
    }

    public String getPriceStr() {
        return priceStr;
    }

    public void setPriceStr(String priceStr) {
        this.priceStr = priceStr;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getIdText() {
        return idText;
    }

    public void setIdText(String idText) {
        this.idText = idText;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Exchange(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
