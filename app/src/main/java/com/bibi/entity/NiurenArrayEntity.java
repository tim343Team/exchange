package com.bibi.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/4/8
 */
public class NiurenArrayEntity {
    private List<NiurenEntity> objs=new ArrayList<>();
    private List<MyNiurenEntity> objsMy=new ArrayList<>();

    public List<NiurenEntity> getObjs() {
        return objs;
    }

    public void setObjs(List<NiurenEntity> objs) {
        this.objs = objs;
    }

    public List<MyNiurenEntity> getObjsMy() {
        return objsMy;
    }

    public void setObjsMy(List<MyNiurenEntity> objsMy) {
        this.objsMy = objsMy;
    }
}
