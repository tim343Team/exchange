package com.bibi.entity;

import java.io.Serializable;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/25
 */
public class CoinBean implements Serializable{
    private String name;

    private String nameCn;

    private String unit;

    private int status;

    private double minTxFee;

    private double cnyRate;

    private double maxTxFee;

    private double usdRate;

    private int sgdRate;

    private int enableRpc;

    private int sort;

    private int canWithdraw;

    private int canRecharge;

    private int canTransfer;

    private int canAutoWithdraw;

    private double withdrawThreshold;

    private double minWithdrawAmount;

    private double maxWithdrawAmount;

    private int isPlatformCoin;

    private boolean hasLegal;

    private String allBalance;

    private String coldWalletAddress;

    private String hotAllBalance;

    private double minerFee;

    private int withdrawScale;

    private double minRechargeAmount;

    private String masterAddress;

    private double maxDailyWithdrawRate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getMinTxFee() {
        return minTxFee;
    }

    public void setMinTxFee(double minTxFee) {
        this.minTxFee = minTxFee;
    }

    public double getCnyRate() {
        return cnyRate;
    }

    public void setCnyRate(double cnyRate) {
        this.cnyRate = cnyRate;
    }

    public double getMaxTxFee() {
        return maxTxFee;
    }

    public void setMaxTxFee(double maxTxFee) {
        this.maxTxFee = maxTxFee;
    }

    public double getUsdRate() {
        return usdRate;
    }

    public void setUsdRate(double usdRate) {
        this.usdRate = usdRate;
    }

    public int getSgdRate() {
        return sgdRate;
    }

    public void setSgdRate(int sgdRate) {
        this.sgdRate = sgdRate;
    }

    public int getEnableRpc() {
        return enableRpc;
    }

    public void setEnableRpc(int enableRpc) {
        this.enableRpc = enableRpc;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getCanWithdraw() {
        return canWithdraw;
    }

    public void setCanWithdraw(int canWithdraw) {
        this.canWithdraw = canWithdraw;
    }

    public int getCanRecharge() {
        return canRecharge;
    }

    public void setCanRecharge(int canRecharge) {
        this.canRecharge = canRecharge;
    }

    public int getCanTransfer() {
        return canTransfer;
    }

    public void setCanTransfer(int canTransfer) {
        this.canTransfer = canTransfer;
    }

    public int getCanAutoWithdraw() {
        return canAutoWithdraw;
    }

    public void setCanAutoWithdraw(int canAutoWithdraw) {
        this.canAutoWithdraw = canAutoWithdraw;
    }

    public double getWithdrawThreshold() {
        return withdrawThreshold;
    }

    public void setWithdrawThreshold(double withdrawThreshold) {
        this.withdrawThreshold = withdrawThreshold;
    }

    public double getMinWithdrawAmount() {
        return minWithdrawAmount;
    }

    public void setMinWithdrawAmount(double minWithdrawAmount) {
        this.minWithdrawAmount = minWithdrawAmount;
    }

    public double getMaxWithdrawAmount() {
        return maxWithdrawAmount;
    }

    public void setMaxWithdrawAmount(double maxWithdrawAmount) {
        this.maxWithdrawAmount = maxWithdrawAmount;
    }

    public int getIsPlatformCoin() {
        return isPlatformCoin;
    }

    public void setIsPlatformCoin(int isPlatformCoin) {
        this.isPlatformCoin = isPlatformCoin;
    }

    public boolean isHasLegal() {
        return hasLegal;
    }

    public void setHasLegal(boolean hasLegal) {
        this.hasLegal = hasLegal;
    }

    public String getAllBalance() {
        return allBalance;
    }

    public void setAllBalance(String allBalance) {
        this.allBalance = allBalance;
    }

    public String getColdWalletAddress() {
        return coldWalletAddress;
    }

    public void setColdWalletAddress(String coldWalletAddress) {
        this.coldWalletAddress = coldWalletAddress;
    }

    public String getHotAllBalance() {
        return hotAllBalance;
    }

    public void setHotAllBalance(String hotAllBalance) {
        this.hotAllBalance = hotAllBalance;
    }

    public double getMinerFee() {
        return minerFee;
    }

    public void setMinerFee(double minerFee) {
        this.minerFee = minerFee;
    }

    public int getWithdrawScale() {
        return withdrawScale;
    }

    public void setWithdrawScale(int withdrawScale) {
        this.withdrawScale = withdrawScale;
    }

    public double getMinRechargeAmount() {
        return minRechargeAmount;
    }

    public void setMinRechargeAmount(double minRechargeAmount) {
        this.minRechargeAmount = minRechargeAmount;
    }

    public String getMasterAddress() {
        return masterAddress;
    }

    public void setMasterAddress(String masterAddress) {
        this.masterAddress = masterAddress;
    }

    public double getMaxDailyWithdrawRate() {
        return maxDailyWithdrawRate;
    }

    public void setMaxDailyWithdrawRate(double maxDailyWithdrawRate) {
        this.maxDailyWithdrawRate = maxDailyWithdrawRate;
    }


}
