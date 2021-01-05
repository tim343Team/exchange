package com.bibi.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import com.bibi.R;
import com.bibi.entity.CoinInfo;
import com.bibi.entity.SimpleListItem;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/4/16
 */
public class SimpleAdapter extends BaseQuickAdapter<SimpleListItem, BaseViewHolder> {
    public SimpleAdapter(int layoutResId, @Nullable List<SimpleListItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SimpleListItem item) {
        helper.setText(R.id.tvCoinName, item.getContent());
        helper.setTextColor(R.id.tvCoinName, item.isSelected()?mContext.getResources().getColor(R.color.colorAccent):mContext.getResources().getColor(R.color.primaryText));
//        helper.setVisible(R.id.ivSellect, item.isSelected());
    }
}
