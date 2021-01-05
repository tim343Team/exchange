package com.bibi.entity;

import java.io.Serializable;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/4/11
 */
public class AnnounceEntity implements Serializable{
    private int id;

    private String title;

    private String content;

    private String createTime;

    private boolean isShow;

    private String imgUrl;

    private int sort;

    private String isTop;

    private int announcementClassification;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getIsTop() {
        return isTop;
    }

    public void setIsTop(String isTop) {
        this.isTop = isTop;
    }

    public int getAnnouncementClassification() {
        return announcementClassification;
    }

    public void setAnnouncementClassification(int announcementClassification) {
        this.announcementClassification = announcementClassification;
    }
}
