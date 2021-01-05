package com.bibi.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/29.
 */

public class Currency implements Serializable {
    private boolean isCollect; // 是否收藏
    private String symbol; // 币种
    private Double open; // 开盘价
    private Double high; // 最高价
    private Double low; // 最低价
    private Double close; // 现价
    private Double chg; // 涨幅
    private Double change; // 改变量24h
    private BigDecimal volume; // 交易量
    private Double turnover; // 交易额
    private Double lastDayClose; // 昨日收盘价
    private Double usdRate;
    private String baseCoin; //1 个BTC 等于多少个USDT
    private String otherCoin;
    private Double baseUsdRate;
    private long time;
    //    private List<Trend> trend ;
    private Double[] trend;
    private String proportion;
    private String closeStr;
    private String cnyPrice;
    private String icon; //币种图片
    private int exchangeable = 1; //1.可交易 2：不可交易

    private int coinScale;

    private int baseCoinScale;

    private PeriodKLineMap periodKLineMap;

    private boolean closed = false;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Double getBaseUsdRate() {
        return baseUsdRate;
    }

    public void setBaseUsdRate(Double baseUsdRate) {
        this.baseUsdRate = baseUsdRate;
    }

    public Double getUsdRate() {
        return usdRate;
    }

    public void setUsdRate(Double usdRate) {
        this.usdRate = usdRate;
    }

    public Currency() {

    }

    public static Currency shallowClone(Currency origin, Currency target) {
        origin.symbol = target.symbol;
        origin.open = target.open;
        origin.high = target.high;
        origin.low = target.low;
        origin.close = target.close;
        origin.closeStr = target.closeStr;
        origin.chg = target.chg;
        origin.change = target.change;
        origin.volume = target.volume;
        origin.turnover = target.turnover;
        origin.lastDayClose = target.lastDayClose;
        origin.cnyPrice = target.cnyPrice;
        return origin;
    }

    public Currency(boolean isCollect) {
        this.isCollect = isCollect;
    }

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

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public Double getChg() {
        return chg;
    }

    public void setChg(Double chg) {
        this.chg = chg;
    }

    public Double getChange() {
        return change;
    }

    public void setChange(Double change) {
        this.change = change;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public Double getTurnover() {
        return turnover;
    }

    public void setTurnover(Double turnover) {
        this.turnover = turnover;
    }

    public String getBaseCoin() {
        return baseCoin;
    }

    public String getOtherCoin() {
        return otherCoin;
    }

    public void setLastDayClose(Double lastDayClose) {
        this.lastDayClose = lastDayClose;
    }

    public void setBaseCoin(String baseCoin) {
        this.baseCoin = baseCoin;
    }

    public void setOtherCoin(String otherCoin) {
        this.otherCoin = otherCoin;
    }

    public static void buildCurrency(List<Currency> currencies) {
        for (Currency currency : currencies) {
            String[] coins = currency.getSymbol().split("/");
            currency.baseCoin = coins[1];
            currency.otherCoin = coins[0];
        }
    }

    public static List<Currency> baseCurrencies(List<Currency> currencies, String baseCoin) {
        List<Currency> result = new ArrayList<>();
        buildCurrency(currencies);
        for (Currency currency : currencies) {
            if (currency.baseCoin.equals(baseCoin)) result.add(currency);
        }
        return result;
    }

    public static List<Currency> baseFavoriteCurrencies(List<Currency> currencies) {
        List<Currency> result = new ArrayList<>();
        buildCurrency(currencies);
        for (Currency currency : currencies) {
            result.add(currency);
        }
        return result;
    }

    public double getLastDayClose() {
        return lastDayClose;
    }

    public void setLastDayClose(double lastDayClose) {
        this.lastDayClose = lastDayClose;
    }

//    public List<Trend> getTrend() {
//        return trend;
//    }
//
//    public void setTrend(List<Trend> trend) {
//        this.trend = trend;
//    }


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

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public int getExchangeable() {
        return exchangeable;
    }

    public void setExchangeable(int exchangeable) {
        this.exchangeable = exchangeable;
    }
}
