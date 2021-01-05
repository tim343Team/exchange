package com.bibi.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/8/11
 */
public class ProfitEntity {
    private double balance;

    private ManagementDetailEntity detail;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public ManagementDetailEntity getDetail() {
        return detail;
    }

    public void setDetail(ManagementDetailEntity detail) {
        this.detail = detail;
    }
}
