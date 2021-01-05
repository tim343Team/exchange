package com.bibi.entity;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/8/10
 */
public class ContractDetailEntity {
    private double amount;
    private double price;
    private String createTime;
    private String coinName;
    private String term;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}
