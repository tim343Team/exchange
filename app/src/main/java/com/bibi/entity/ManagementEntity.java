package com.bibi.entity;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/8/10
 */
public class ManagementEntity {
    private String type;
    private double amount;
    private String createTime;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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
}
