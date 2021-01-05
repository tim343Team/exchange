package com.bibi.ui.mychart;

/**
 * Created by loro on 2017/2/8.
 */
public class KLineBean {
    public long time;
    public String date;
    public float open;
    public float close;
    public float high;
    public float low;
    public float vol;

    public KLineBean(long time,String date, float open, float close, float high, float low, float vol) {
        this.time = time;
        this.date = date;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
        this.vol = vol;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setOpen(float open) {
        this.open = open;
    }

    public void setClose(float close) {
        this.close = close;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public void setVol(float vol) {
        this.vol = vol;
    }

    public String getDate() {
        return date;
    }

    public float getOpen() {
        return open;
    }

    public float getClose() {
        return close;
    }

    public float getHigh() {
        return high;
    }

    public float getLow() {
        return low;
    }

    public float getVol() {
        return vol;
    }
}
