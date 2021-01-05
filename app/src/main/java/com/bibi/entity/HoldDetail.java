package com.bibi.entity;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/24
 */
public class HoldDetail {
    private String orderId;

    private double price;

    private double amount;

    private double turnover;

    private double fee;

    private double time;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getTurnover() {
        return turnover;
    }

    public void setTurnover(double turnover) {
        this.turnover = turnover;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }
}
