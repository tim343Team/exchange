package com.bibi.entity;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/6/11
 */
public class OptionEntity {
    private String orderId;

    private int memberId;

    private double amount;

    private String symbol;

    private String coinSymbol;

    private String baseSymbol;

    private String status;

    private String direction;

    private double price;

    private double settlementPrice;

    private long time;

    private String completedTime;

    private String updateTime;

    private double profitLost;

    private double expectedProfit;

    private double profitRate;

    private double commission;

    private long settlementTime;

    private String period;

    private String winFlag;

    private String coinIcon;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    public String getBaseSymbol() {
        return baseSymbol;
    }

    public void setBaseSymbol(String baseSymbol) {
        this.baseSymbol = baseSymbol;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSettlementPrice() {
        return settlementPrice;
    }

    public void setSettlementPrice(double settlementPrice) {
        this.settlementPrice = settlementPrice;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(String completedTime) {
        this.completedTime = completedTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public double getProfitLost() {
        return profitLost;
    }

    public void setProfitLost(double profitLost) {
        this.profitLost = profitLost;
    }

    public double getExpectedProfit() {
        return expectedProfit;
    }

    public void setExpectedProfit(double expectedProfit) {
        this.expectedProfit = expectedProfit;
    }

    public double getProfitRate() {
        return profitRate;
    }

    public void setProfitRate(double profitRate) {
        this.profitRate = profitRate;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public long getSettlementTime() {
        return settlementTime;
    }

    public void setSettlementTime(long settlementTime) {
        this.settlementTime = settlementTime;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getWinFlag() {
        return winFlag;
    }

    public void setWinFlag(String winFlag) {
        this.winFlag = winFlag;
    }

    public String getCoinIcon() {
        return coinIcon;
    }

    public void setCoinIcon(String coinIcon) {
        this.coinIcon = coinIcon;
    }

}
