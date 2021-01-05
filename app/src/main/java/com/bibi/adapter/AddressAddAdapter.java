package com.bibi.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import com.bibi.R;
import com.bibi.entity.Address;
import com.bibi.entity.TiBiAddressContent;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/16
 */
public class AddressAddAdapter extends BaseQuickAdapter<TiBiAddressContent, BaseViewHolder> {
    public AddressAddAdapter(int layoutResId, @Nullable List<TiBiAddressContent> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final TiBiAddressContent item) {
            helper.getView(R.id.tvDelete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete.onDelete(item);
                }
            });
            helper.setText(R.id.tvAddress,item.getAddress());
//        helper.addOnClickListener(R.id.tvDelete);
    }

    OnclickListenerDelete delete;

    public void setOnDeleteListener(OnclickListenerDelete delete) {
        this.delete = delete;
    }

    public interface OnclickListenerDelete {

        void onDelete(TiBiAddressContent item);
    }
}
