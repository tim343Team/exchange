package com.bibi.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import com.bibi.R;
import com.bibi.entity.ContractDetailEntity;


/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/8/10
 */
public class ContractDetailAdapter extends BaseQuickAdapter<ContractDetailEntity, BaseViewHolder> {
    private Context context;

    public ContractDetailAdapter(Context context, int layoutResId, @Nullable List<ContractDetailEntity> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ContractDetailEntity item) {
        helper.setText(R.id.tvAmount, item.getAmount() + "");
        helper.setText(R.id.tvTerm, item.getTerm() + "");
        helper.setText(R.id.tvFinishTime, item.getCreateTime() + "");
    }
}
