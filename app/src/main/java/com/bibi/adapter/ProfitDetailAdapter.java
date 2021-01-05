package com.bibi.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.DecimalFormat;
import java.util.List;

import com.bibi.R;
import com.bibi.entity.ContractDetailEntity;
import com.bibi.entity.ProfitDetailEntity;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/8/10
 */
public class ProfitDetailAdapter extends BaseQuickAdapter<ProfitDetailEntity, BaseViewHolder> {
    private Context context;

    public ProfitDetailAdapter(Context context, int layoutResId, @Nullable List<ProfitDetailEntity> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ProfitDetailEntity item) {
        helper.setText(R.id.tvProfit, new DecimalFormat("#0.0000").format(item.getAmount()) + "");
        helper.setText(R.id.tvCreatTime, item.getCreateTime());
    }
}
