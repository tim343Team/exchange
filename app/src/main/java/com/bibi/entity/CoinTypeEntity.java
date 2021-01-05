package com.bibi.entity;

import java.io.Serializable;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/22
 */
public class CoinTypeEntity  implements Serializable {
    private String symbol;

    private String coinSymbol;

    private String baseSymbol;

    private String leverage;

    private int enable;

    private double fee;

    private String baseFee;

    private int sort;

    private int coinScale;

    private String exchangeType;

    private int baseCoinScale;

    private double minSellPrice;

    private int enableMarketSell;

    private int enableMarketBuy;

    private int maxTradingTime;

    private int maxTradingOrder;

    private int flag;

    private double minTurnover;

    private int zone;

    private double minVolume;

    private double maxVolume;

    private String defaultSymbol;

    private double spread;

    private double overnight;

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

    public String getLeverage() {
        return leverage;
    }

    public void setLeverage(String leverage) {
        this.leverage = leverage;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getBaseFee() {
        return baseFee;
    }

    public void setBaseFee(String baseFee) {
        this.baseFee = baseFee;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getCoinScale() {
        return coinScale;
    }

    public void setCoinScale(int coinScale) {
        this.coinScale = coinScale;
    }

    public String getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
    }

    public int getBaseCoinScale() {
        return baseCoinScale;
    }

    public void setBaseCoinScale(int baseCoinScale) {
        this.baseCoinScale = baseCoinScale;
    }

    public double getMinSellPrice() {
        return minSellPrice;
    }

    public void setMinSellPrice(double minSellPrice) {
        this.minSellPrice = minSellPrice;
    }

    public int getEnableMarketSell() {
        return enableMarketSell;
    }

    public void setEnableMarketSell(int enableMarketSell) {
        this.enableMarketSell = enableMarketSell;
    }

    public int getEnableMarketBuy() {
        return enableMarketBuy;
    }

    public void setEnableMarketBuy(int enableMarketBuy) {
        this.enableMarketBuy = enableMarketBuy;
    }

    public int getMaxTradingTime() {
        return maxTradingTime;
    }

    public void setMaxTradingTime(int maxTradingTime) {
        this.maxTradingTime = maxTradingTime;
    }

    public int getMaxTradingOrder() {
        return maxTradingOrder;
    }

    public void setMaxTradingOrder(int maxTradingOrder) {
        this.maxTradingOrder = maxTradingOrder;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public double getMinTurnover() {
        return minTurnover;
    }

    public void setMinTurnover(double minTurnover) {
        this.minTurnover = minTurnover;
    }

    public int getZone() {
        return zone;
    }

    public void setZone(int zone) {
        this.zone = zone;
    }

    public double getMinVolume() {
        return minVolume;
    }

    public void setMinVolume(double minVolume) {
        this.minVolume = minVolume;
    }

    public double getMaxVolume() {
        return maxVolume;
    }

    public void setMaxVolume(double maxVolume) {
        this.maxVolume = maxVolume;
    }

    public String getDefaultSymbol() {
        return defaultSymbol;
    }

    public void setDefaultSymbol(String defaultSymbol) {
        this.defaultSymbol = defaultSymbol;
    }

    public double getSpread() {
        return spread;
    }

    public void setSpread(double spread) {
        this.spread = spread;
    }

    public double getOvernight() {
        return overnight;
    }

    public void setOvernight(double overnight) {
        this.overnight = overnight;
    }
}
