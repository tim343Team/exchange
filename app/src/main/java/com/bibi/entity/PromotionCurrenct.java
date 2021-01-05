package com.bibi.entity;

import java.util.List;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/6/21
 */
public class PromotionCurrenct {
    private String symbol;

    private double open;

    private double high;

    private double low;

    private double close;

    private double chg;

    private double change;

    private double volume;

    private double turnover;

    private int time;

    private int lastDayClose;

    private double usdRate;

    private int baseUsdRate;

    private String closeStr;

    private Double[] trend ;

    private String proportion;

    private String cnyPrice;

    private String icon;

    private int coinScale;

    private int baseCoinScale;

    private PeriodKLineMap periodKLineMap;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getChg() {
        return chg;
    }

    public void setChg(double chg) {
        this.chg = chg;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getTurnover() {
        return turnover;
    }

    public void setTurnover(double turnover) {
        this.turnover = turnover;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getLastDayClose() {
        return lastDayClose;
    }

    public void setLastDayClose(int lastDayClose) {
        this.lastDayClose = lastDayClose;
    }

    public double getUsdRate() {
        return usdRate;
    }

    public void setUsdRate(double usdRate) {
        this.usdRate = usdRate;
    }

    public int getBaseUsdRate() {
        return baseUsdRate;
    }

    public void setBaseUsdRate(int baseUsdRate) {
        this.baseUsdRate = baseUsdRate;
    }

    public String getCloseStr() {
        return closeStr;
    }

    public void setCloseStr(String closeStr) {
        this.closeStr = closeStr;
    }

    public Double[] getTrend() {
        return trend;
    }

    public void setTrend(Double[] trend) {
        this.trend = trend;
    }

    public String getProportion() {
        return proportion;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
    }

    public String getCnyPrice() {
        return cnyPrice;
    }

    public void setCnyPrice(String cnyPrice) {
        this.cnyPrice = cnyPrice;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getCoinScale() {
        return coinScale;
    }

    public void setCoinScale(int coinScale) {
        this.coinScale = coinScale;
    }

    public int getBaseCoinScale() {
        return baseCoinScale;
    }

    public void setBaseCoinScale(int baseCoinScale) {
        this.baseCoinScale = baseCoinScale;
    }

    public PeriodKLineMap getPeriodKLineMap() {
        return periodKLineMap;
    }

    public void setPeriodKLineMap(PeriodKLineMap periodKLineMap) {
        this.periodKLineMap = periodKLineMap;
    }
}
