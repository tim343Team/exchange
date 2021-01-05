package com.bibi.entity;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/30
 */
public class ContractEntity {
    private int id;

    private String title;

    private int sysHelpClassification;

    private String imgUrl;

    private String createTime;

    private int status;

    private String content;

    private String author;

    private int sort;

    private String isTop;

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

    public int getSysHelpClassification() {
        return sysHelpClassification;
    }

    public void setSysHelpClassification(int sysHelpClassification) {
        this.sysHelpClassification = sysHelpClassification;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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
}
