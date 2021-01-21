package com.bibi.adapter;

import com.github.tifezh.kchartlib.chart.BaseKChartAdapter;

import java.util.ArrayList;
import java.util.List;

import com.bibi.entity.KLineEntity;

/**
 * 数据适配器
 * Created by tifezh on 2016/6/18.
 */

public class KChartAdapter extends BaseKChartAdapter {
    private List<KLineEntity> datas = new ArrayList<>();

    public KChartAdapter() {

    }

    public List<KLineEntity> getDatas() {
        return datas;
    }

    public KChartAdapter(List<KLineEntity> datas) {
        this.datas = datas;
    }

    @Override
    public int getCount() {
        if (datas != null)
            return datas.size();
        return 0;
    }

    public void setDatas(List<KLineEntity> datas) {
        this.datas = datas;
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public String getDate(int position) {
        try {
            String s = datas.get(position).getDatetime();
//            String[] split = s.split("-");
//            Date date = new Date(s);
//            date.setYear(Integer.parseInt(split[0]) - 1900);
//            date.setMonth(Integer.parseInt(split[1]) - 1);
//            date.setDate(Integer.parseInt(split[2]));
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 向尾部添加数据
     */
    public void addData(List<KLineEntity> data) {
        if (data != null && !data.isEmpty()) {
            datas.clear();
            datas.addAll( data);
            notifyDataSetChanged();
        }
    }

    /**
     * 向头部添加数据
     */
    public void addHeaderData(List<KLineEntity> data) {
        if (data != null && !data.isEmpty()) {
            datas.addAll(0, data);
            notifyDataSetChanged();
        }
    }

    /**
     * 向尾部添加数据
     */
    public void addFooterData(List<KLineEntity> data) {
        if (data != null && !data.isEmpty()) {
            datas.addAll( data);
            notifyDataSetChanged();
        }
    }

    /**
     * 向尾部更新数据数据
     */
    public void updateFooterData(List<KLineEntity> data) {
        if (data != null && !data.isEmpty()) {
            datas.remove(datas.size()-1);
            datas.addAll( data);
            notifyDataSetChanged();
        }
    }

    /**
     * 改变某个点的值
     *
     * @param position 索引值
     */
    public void changeItem(int position, KLineEntity data) {
        datas.set(position, data);
        notifyDataSetChanged();
    }

    /**
     * 改变最后个点的值
     *
     * @param position 索引值
     */
    public void changeItem(KLineEntity data) {
        if (datas != null && !datas.isEmpty()) {
            datas.set(datas.size() - 1, data);
            notifyDataSetChanged();
        }
    }

}
