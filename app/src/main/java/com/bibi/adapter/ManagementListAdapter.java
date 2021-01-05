package com.bibi.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.bibi.entity.ProfitDetailEntity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;

import com.bibi.R;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/8/10
 */
public class ManagementListAdapter extends BaseQuickAdapter<ProfitDetailEntity, BaseViewHolder> {
    private Context context;

    public ManagementListAdapter(Context context,int layoutResId, @Nullable List<ProfitDetailEntity> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ProfitDetailEntity item) {
        helper.setText(R.id.tvType,item.getCoinName());
        helper.setText(R.id.tvAmount,item.getAmount()+"");
        helper.setText(R.id.tvCreatTime,item.getCreateTime());
    }
}
