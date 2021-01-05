package com.bibi.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import com.bibi.R;
import com.bibi.entity.BankName;
import com.bibi.entity.ScoreRecordBean;


public class ScoreRecordAdapter extends BaseQuickAdapter<ScoreRecordBean, BaseViewHolder> {
    public ScoreRecordAdapter(int layoutResId, @Nullable List<ScoreRecordBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ScoreRecordBean item) {
        helper.setText(R.id.tvSmobl, item.getSymbol());
        helper.setText(R.id.tv_score_record_amount, item.getAmount()+"");
        helper.setText(R.id.tv_score_record_time, item.getCreateTime());
    }

}

