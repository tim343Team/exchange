package com.bibi.entity;

import java.io.Serializable;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/23
 */
public class AssetEntity implements Serializable{
    private double dynamicProfitPercent;

    private double dynamicProfit;

    private double riskRate;

    private double available;

    private double floatPnL;

    private double frozon;

    private double balance;

    public double getDynamicProfit() {
        return dynamicProfit;
    }

    public void setDynamicProfit(double dynamicProfit) {
        this.dynamicProfit = dynamicProfit;
    }

    public double getDynamicProfitPercent() {
        return dynamicProfitPercent;
    }

    public void setDynamicProfitPercent(double dynamicProfitPercent) {
        this.dynamicProfitPercent = dynamicProfitPercent;
    }

    public double getRiskRate() {
        return riskRate;
    }

    public void setRiskRate(double riskRate) {
        this.riskRate = riskRate;
    }

    public double getAvailable() {
        return available;
    }

    public void setAvailable(double available) {
        this.available = available;
    }

    public double getFloatPnL() {
        return floatPnL;
    }

    public void setFloatPnL(double floatPnL) {
        this.floatPnL = floatPnL;
    }

    public double getFrozon() {
        return frozon;
    }

    public void setFrozon(double frozon) {
        this.frozon = frozon;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
