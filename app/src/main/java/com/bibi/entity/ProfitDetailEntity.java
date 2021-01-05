package com.bibi.entity;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/8/10
 */
public class ProfitDetailEntity {
    private double amount;
    private String createTime;
    private String coinName;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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
}
