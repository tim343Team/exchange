package com.bibi.entity;

import java.io.Serializable;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/31
 */
public class ContractCoin implements Serializable {
    private int id;

    private String createTime;

    private String deleteTime;

    private CoinBean coin;

    private String address;

    private int status;

    private int memberId;

    private String remark;

    private String tag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(String deleteTime) {
        this.deleteTime = deleteTime;
    }

    public CoinBean getCoin() {
        return coin;
    }

    public void setCoin(CoinBean coin) {
        this.coin = coin;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
