package com.bibi.entity;

import java.util.List;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/30
 */
public class RecordEntity {
    private List<ScoreRecordBean> content ;

    private int number;

    private int size;

    private int totalElements;

    public List<ScoreRecordBean> getContent() {
        return content;
    }

    public void setContent(List<ScoreRecordBean> content) {
        this.content = content;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }
}
