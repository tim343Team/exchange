package com.bibi.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/8/11
 */
public class ManagementDetailEntity {
    private List<ManagementEntity> content=new ArrayList<>();

    public List<ManagementEntity> getContent() {
        return content;
    }

    public void setContent(List<ManagementEntity> content) {
        this.content = content;
    }
}
