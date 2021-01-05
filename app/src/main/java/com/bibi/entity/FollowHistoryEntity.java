package com.bibi.entity;

import java.io.Serializable;
import java.util.List;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/22
 */
public class FollowHistoryEntity implements Serializable{
    private List<FollowHistoryContent> content ;

    private boolean last;

    private int totalPages;

    private int totalElements;

    private boolean first;

    private String sort;

    private int numberOfElements;

    private int size;

    private int number;

    public List<FollowHistoryContent> getContent() {
        return content;
    }

    public void setContent(List<FollowHistoryContent> content) {
        this.content = content;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
