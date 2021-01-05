package com.bibi.entity;

import java.io.Serializable;
import java.util.List;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/24
 */
public class HoldEntity implements Serializable{
    private String orderId;

    private double memberId;

    private String type;

    private double amount;

    private String symbol;

    private double tradedAmount;

    private double turnover;

    private String coinSymbol;

    private String baseSymbol;

    private String status;

    private String direction;

    private String directionFlag;

    private double price;

    private double currentPrice;

    private double triggerPrice;

    private long time;

    private Long completedTime;

    private String canceledTime;

    private double marginTrade;

    private String orderResource;

    private double stopProfitPrice;

    private double stopLossPrice;

    private double originPrice;

    private int leverage;

    private double marginMoney;

    private double overnightRate;

    private double overnightMoney;

    private double settled;

    private double followId;

    private String followOrderId;

    private int followStatus;

    private Long updateTime;

    private double positionAvgPrice;

    private double positionMargin;

    private double profitLost;

    private String positionStatus;

    private double orderFee;

    private double currentFee;

    private List<HoldDetail> detail ;

    private String amountStr;

    private String priceStr;

    private boolean completed;

    private String coinIcon;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public double getMemberId() {
        return memberId;
    }

    public void setMemberId(double memberId) {
        this.memberId = memberId;
    }

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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getTradedAmount() {
        return tradedAmount;
    }

    public void setTradedAmount(double tradedAmount) {
        this.tradedAmount = tradedAmount;
    }

    public double getTurnover() {
        return turnover;
    }

    public void setTurnover(double turnover) {
        this.turnover = turnover;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDirectionFlag() {
        return directionFlag;
    }

    public void setDirectionFlag(String directionFlag) {
        this.directionFlag = directionFlag;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTriggerPrice() {
        return triggerPrice;
    }

    public void setTriggerPrice(double triggerPrice) {
        this.triggerPrice = triggerPrice;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Long getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(Long completedTime) {
        this.completedTime = completedTime;
    }

    public String getCanceledTime() {
        return canceledTime;
    }

    public void setCanceledTime(String canceledTime) {
        this.canceledTime = canceledTime;
    }

    public double getMarginTrade() {
        return marginTrade;
    }

    public void setMarginTrade(double marginTrade) {
        this.marginTrade = marginTrade;
    }

    public String getOrderResource() {
        return orderResource;
    }

    public void setOrderResource(String orderResource) {
        this.orderResource = orderResource;
    }

    public double getStopProfitPrice() {
        return stopProfitPrice;
    }

    public void setStopProfitPrice(double stopProfitPrice) {
        this.stopProfitPrice = stopProfitPrice;
    }

    public double getStopLossPrice() {
        return stopLossPrice;
    }

    public void setStopLossPrice(double stopLossPrice) {
        this.stopLossPrice = stopLossPrice;
    }

    public double getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(double originPrice) {
        this.originPrice = originPrice;
    }

    public int getLeverage() {
        return leverage;
    }

    public void setLeverage(int leverage) {
        this.leverage = leverage;
    }

    public double getMarginMoney() {
        return marginMoney;
    }

    public void setMarginMoney(double marginMoney) {
        this.marginMoney = marginMoney;
    }

    public double getOvernightRate() {
        return overnightRate;
    }

    public void setOvernightRate(double overnightRate) {
        this.overnightRate = overnightRate;
    }

    public double getOvernightMoney() {
        return overnightMoney;
    }

    public void setOvernightMoney(double overnightMoney) {
        this.overnightMoney = overnightMoney;
    }

    public double getSettled() {
        return settled;
    }

    public void setSettled(double settled) {
        this.settled = settled;
    }

    public double getFollowId() {
        return followId;
    }

    public void setFollowId(double followId) {
        this.followId = followId;
    }

    public String getFollowOrderId() {
        return followOrderId;
    }

    public void setFollowOrderId(String followOrderId) {
        this.followOrderId = followOrderId;
    }

    public int getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(int followStatus) {
        this.followStatus = followStatus;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public double getPositionAvgPrice() {
        return positionAvgPrice;
    }

    public void setPositionAvgPrice(double positionAvgPrice) {
        this.positionAvgPrice = positionAvgPrice;
    }

    public double getPositionMargin() {
        return positionMargin;
    }

    public void setPositionMargin(double positionMargin) {
        this.positionMargin = positionMargin;
    }

    public double getProfitLost() {
        return profitLost;
    }

    public void setProfitLost(double profitLost) {
        this.profitLost = profitLost;
    }

    public String getPositionStatus() {
        return positionStatus;
    }

    public void setPositionStatus(String positionStatus) {
        this.positionStatus = positionStatus;
    }

    public double getOrderFee() {
        return orderFee;
    }

    public void setOrderFee(double orderFee) {
        this.orderFee = orderFee;
    }

    public double getCurrentFee() {
        return currentFee;
    }

    public void setCurrentFee(double currentFee) {
        this.currentFee = currentFee;
    }

    public List<HoldDetail> getDetail() {
        return detail;
    }

    public void setDetail(List<HoldDetail> detail) {
        this.detail = detail;
    }

    public String getAmountStr() {
        return amountStr;
    }

    public void setAmountStr(String amountStr) {
        this.amountStr = amountStr;
    }

    public String getPriceStr() {
        return priceStr;
    }

    public void setPriceStr(String priceStr) {
        this.priceStr = priceStr;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getCoinIcon() {
        return coinIcon;
    }

    public void setCoinIcon(String coinIcon) {
        this.coinIcon = coinIcon;
    }
}
