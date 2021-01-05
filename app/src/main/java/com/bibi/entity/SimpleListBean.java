package com.bibi.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/4/16
 */
public class SimpleListBean implements Serializable {
    private List<SimpleListItem> newsItems=new ArrayList<>();

    public List<SimpleListItem> getNewsItems() {
        return newsItems;
    }

    public void setNewsItems(List<SimpleListItem> newsItems) {
        this.newsItems = newsItems;
    }

    public void addItem(SimpleListItem item){
        newsItems.add(item);
    }
}
