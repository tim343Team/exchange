package com.bibi.entity;

import java.io.Serializable;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/25
 */
public class CoinContract implements Serializable{
    private int id;

    private int memberId;

    private CoinBean coin;

    private double balance;

    private double frozenBalance;

    private double presentBalance;

    private String address;

    private int isLock;

    private String releaseBalance;

    private int isHold;

    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public CoinBean getCoin() {
        return coin;
    }

    public void setCoin(CoinBean coin) {
        this.coin = coin;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getFrozenBalance() {
        return frozenBalance;
    }

    public void setFrozenBalance(double frozenBalance) {
        this.frozenBalance = frozenBalance;
    }

    public double getPresentBalance() {
        return presentBalance;
    }

    public void setPresentBalance(double presentBalance) {
        this.presentBalance = presentBalance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIsLock() {
        return isLock;
    }

    public void setIsLock(int isLock) {
        this.isLock = isLock;
    }

    public String getReleaseBalance() {
        return releaseBalance;
    }

    public void setReleaseBalance(String releaseBalance) {
        this.releaseBalance = releaseBalance;
    }

    public int getIsHold() {
        return isHold;
    }

    public void setIsHold(int isHold) {
        this.isHold = isHold;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
