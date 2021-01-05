package com.bibi.entity;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/10/12
 */
public class InvestTypeEntity {
    private String coinName;
    private String term;
    private String termCnName;
    private String rate;
    private String min;
    private String max;

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getTermCnName() {
        return termCnName;
    }

    public void setTermCnName(String termCnName) {
        this.termCnName = termCnName;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }
}

