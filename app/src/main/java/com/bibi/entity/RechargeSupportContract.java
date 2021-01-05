package com.bibi.entity;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/8/1
 */
public class RechargeSupportContract {
    private String minWithdrawAmount;

    private String maxWithdrawAmount;

    private String name;

    private String withdrawThreshold;

    private String nameCn;

    private String enableWithdraw;

    private String enableRecharge;

    private String withdrawFee;

    private double txFee;

    private double txFeeRatio;

    public String getMinWithdrawAmount() {
        return minWithdrawAmount;
    }

    public void setMinWithdrawAmount(String minWithdrawAmount) {
        this.minWithdrawAmount = minWithdrawAmount;
    }

    public String getMaxWithdrawAmount() {
        return maxWithdrawAmount;
    }

    public void setMaxWithdrawAmount(String maxWithdrawAmount) {
        this.maxWithdrawAmount = maxWithdrawAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWithdrawThreshold() {
        return withdrawThreshold;
    }

    public void setWithdrawThreshold(String withdrawThreshold) {
        this.withdrawThreshold = withdrawThreshold;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getEnableWithdraw() {
        return enableWithdraw;
    }

    public void setEnableWithdraw(String enableWithdraw) {
        this.enableWithdraw = enableWithdraw;
    }

    public String getEnableRecharge() {
        return enableRecharge;
    }

    public void setEnableRecharge(String enableRecharge) {
        this.enableRecharge = enableRecharge;
    }

    public String getWithdrawFee() {
        return withdrawFee;
    }

    public void setWithdrawFee(String withdrawFee) {
        this.withdrawFee = withdrawFee;
    }

    public double getTxFee() {
        return txFee;
    }

    public void setTxFee(double txFee) {
        this.txFee = txFee;
    }

    public double getTxFeeRatio() {
        return txFeeRatio;
    }

    public void setTxFeeRatio(double txFeeRatio) {
        this.txFeeRatio = txFeeRatio;
    }
}
