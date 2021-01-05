package com.bibi.entity;

import java.math.BigDecimal;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/6/16
 */
public class CurrencyK {
    private boolean isCollect; // 是否收藏
    private String symbol; // 币种
    private Double open24; // 开盘价
    private Double high24; // 最高价
    private Double low24; // 最低价
    private Double close; // 现价
    private Double chg24; // 涨幅
    private Double change24; // 改变量24h
    private BigDecimal volume24; // 交易量
    private Double turnover; // 交易额
    private Double lastDayClose; // 昨日收盘价
    private Double usdRate;
    private String baseCoin; //1 个BTC 等于多少个USDT
    private String otherCoin;
    private Double baseUsdRate ;
    private long time;
    //    private List<Trend> trend ;
    private Double[] trend ;
    private String proportion;
    private String closeStr;
    private String cnyPrice;
    private String icon; //币种图片

    public boolean isCollect() {
        return isCollect;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getOpen24() {
        return open24;
    }

    public void setOpen24(Double open24) {
        this.open24 = open24;
    }

    public Double getHigh24() {
        return high24;
    }

    public void setHigh24(Double high24) {
        this.high24 = high24;
    }

    public Double getLow24() {
        return low24;
    }

    public void setLow24(Double low24) {
        this.low24 = low24;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public Double getChg24() {
        return chg24;
    }

    public void setChg24(Double chg24) {
        this.chg24 = chg24;
    }

    public Double getChange24() {
        return change24;
    }

    public void setChange24(Double change24) {
        this.change24 = change24;
    }

    public BigDecimal getVolume24() {
        return volume24;
    }

    public void setVolume24(BigDecimal volume24) {
        this.volume24 = volume24;
    }

    public Double getTurnover() {
        return turnover;
    }

    public void setTurnover(Double turnover) {
        this.turnover = turnover;
    }

    public Double getLastDayClose() {
        return lastDayClose;
    }

    public void setLastDayClose(Double lastDayClose) {
        this.lastDayClose = lastDayClose;
    }

    public Double getUsdRate() {
        return usdRate;
    }

    public void setUsdRate(Double usdRate) {
        this.usdRate = usdRate;
    }

    public String getBaseCoin() {
        return baseCoin;
    }

    public void setBaseCoin(String baseCoin) {
        this.baseCoin = baseCoin;
    }

    public String getOtherCoin() {
        return otherCoin;
    }

    public void setOtherCoin(String otherCoin) {
        this.otherCoin = otherCoin;
    }

    public Double getBaseUsdRate() {
        return baseUsdRate;
    }

    public void setBaseUsdRate(Double baseUsdRate) {
        this.baseUsdRate = baseUsdRate;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
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

    public String getCloseStr() {
        return closeStr;
    }

    public void setCloseStr(String closeStr) {
        this.closeStr = closeStr;
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
}
