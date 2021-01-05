package com.bibi.entity;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/27
 */
public class SummaryEntity {
    private String promotionExchangeAmount;

    private double promotionAwardAmount;

    private double totalExchangeAmount;

    private int inviteCount;

    private int count;

    private int totalInviteCount;

    private String lastweekPromotionExchangeAmount;

    public String getPromotionExchangeAmount() {
        return promotionExchangeAmount;
    }

    public void setPromotionExchangeAmount(String promotionExchangeAmount) {
        this.promotionExchangeAmount = promotionExchangeAmount;
    }

    public double getPromotionAwardAmount() {
        return promotionAwardAmount;
    }

    public void setPromotionAwardAmount(double promotionAwardAmount) {
        this.promotionAwardAmount = promotionAwardAmount;
    }

    public double getTotalExchangeAmount() {
        return totalExchangeAmount;
    }

    public void setTotalExchangeAmount(double totalExchangeAmount) {
        this.totalExchangeAmount = totalExchangeAmount;
    }

    public int getInviteCount() {
        return inviteCount;
    }

    public void setInviteCount(int inviteCount) {
        this.inviteCount = inviteCount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotalInviteCount() {
        return totalInviteCount;
    }

    public void setTotalInviteCount(int totalInviteCount) {
        this.totalInviteCount = totalInviteCount;
    }

    public String getLastweekPromotionExchangeAmount() {
        return lastweekPromotionExchangeAmount;
    }

    public void setLastweekPromotionExchangeAmount(String lastweekPromotionExchangeAmount) {
        this.lastweekPromotionExchangeAmount = lastweekPromotionExchangeAmount;
    }
}
