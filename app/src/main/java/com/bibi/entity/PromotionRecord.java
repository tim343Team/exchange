package com.bibi.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public class PromotionRecord {

    /**
     * createTime : 2018-03-27 19:36:45
     * username : charlzon
     * level : 0
     */

    private String createTime;

    private String mobilePhone;

    private String username;

    private String grade;

    private int inviteCount;

    private int id;

    private boolean isSelect;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getInviteCount() {
        return inviteCount;
    }

    public void setInviteCount(int inviteCount) {
        this.inviteCount = inviteCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
