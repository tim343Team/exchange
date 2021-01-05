package com.bibi.entity;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/22
 */
public class FollowHistoryContent {
    private long id;

    private MemberFollow member;

    private double type;

    private long status;

    private double direction;

    private double createTime;

    private double updateTime;

    private double leverage;

    private double amount;

    private String symbol;

    private String profit;

    private FmemberFollow fmember;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public MemberFollow getMember() {
        return member;
    }

    public void setMember(MemberFollow member) {
        this.member = member;
    }

    public double getType() {
        return type;
    }

    public void setType(double type) {
        this.type = type;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public double getCreateTime() {
        return createTime;
    }

    public void setCreateTime(double createTime) {
        this.createTime = createTime;
    }

    public double getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(double updateTime) {
        this.updateTime = updateTime;
    }

    public double getLeverage() {
        return leverage;
    }

    public void setLeverage(double leverage) {
        this.leverage = leverage;
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

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public FmemberFollow getFmember() {
        return fmember;
    }

    public void setFmember(FmemberFollow fmember) {
        this.fmember = fmember;
    }
}
