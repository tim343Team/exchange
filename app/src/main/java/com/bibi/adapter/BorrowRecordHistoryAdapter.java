package com.bibi.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import com.bibi.R;
import com.bibi.entity.BorrowRecordBean;
import com.bibi.entity.GiveBackRecordBean;


public class BorrowRecordHistoryAdapter extends BaseQuickAdapter<GiveBackRecordBean, BaseViewHolder> {
    public BorrowRecordHistoryAdapter(int layoutResId, @Nullable List<GiveBackRecordBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GiveBackRecordBean item) {
        helper.setText(R.id.tv_borrow_record_coin, item.getCoin().getUnit())
                .setText(R.id.tv_borrow_record_amount, item.getAmount() + "")
                .setText(R.id.tv_borrow_record_time, item.getCreateTime())
                .setText(R.id.tv_borrow_record_interest, item.getInterest() + "");
    }
}
