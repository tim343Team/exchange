package com.bibi.entity;

import java.util.List;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/27
 */
public class TiBiAddress {
    private List<TiBiAddressContent> content ;

    private boolean last;

    private int totalPages;

    private int totalElements;

    private List<Sort> sort ;

    private boolean first;

    private int numberOfElements;

    private int size;

    private int number;

    public List<TiBiAddressContent> getContent() {
        return content;
    }

    public void setContent(List<TiBiAddressContent> content) {
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

    public List<Sort> getSort() {
        return sort;
    }

    public void setSort(List<Sort> sort) {
        this.sort = sort;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
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
