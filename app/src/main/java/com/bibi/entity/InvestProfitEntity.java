package com.bibi.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/8/11
 */
public class InvestProfitEntity {
    private String memberId;
    private String coinName;
    private double totalProfit;
    private double todayProfit;
    private double totalInvest;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public double getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(double totalProfit) {
        this.totalProfit = totalProfit;
    }

    public double getTodayProfit() {
        return todayProfit;
    }

    public void setTodayProfit(double todayProfit) {
        this.todayProfit = todayProfit;
    }

    public double getTotalInvest() {
        return totalInvest;
    }

    public void setTotalInvest(double totalInvest) {
        this.totalInvest = totalInvest;
    }

}
