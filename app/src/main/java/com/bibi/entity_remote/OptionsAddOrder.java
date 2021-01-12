package com.bibi.entity_remote;

/**
 * $
 * directionBUY 买涨，  SELL 买跌
 amount 数量
 symbol 交易对
 price 下单时价格
 period 下单时间
 coinId 下单币种
 *
 * @author weiqiliu
 * @version 1.0 2020/6/18
 */
public class OptionsAddOrder {
    private String token;
    private String direction;
    private String amount;
    private String symbol;
    private String price;
    private int period;
    private String coinId;
    private int leverage;

    public OptionsAddOrder() {
    }

    public OptionsAddOrder(String token, String direction, String amount, String symbol, String price, String coinId) {
        this.token = token;
        this.direction = direction;
        this.amount = amount;
        this.symbol = symbol;
        this.price = price;
        this.coinId = coinId;
    }

    public OptionsAddOrder(String token, String direction, String amount, String symbol, String price, String coinId,int leverage) {
        this.token = token;
        this.direction = direction;
        this.amount = amount;
        this.symbol = symbol;
        this.price = price;
        this.coinId = coinId;
        this.leverage = leverage;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public int getLeverage() {
        return leverage;
    }

    public void setLeverage(int leverage) {
        this.leverage = leverage;
    }
}
