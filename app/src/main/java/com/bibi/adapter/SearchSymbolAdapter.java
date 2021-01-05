package com.bibi.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bibi.R;
import com.bibi.entity.Currency;
import com.bibi.utils.WonderfulToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/10/7
 */
public class SearchSymbolAdapter extends BaseQuickAdapter<Currency, BaseViewHolder> {

    public SearchSymbolAdapter(@Nullable List<Currency> data) {
        super(R.layout.adapter_layout_search_symbol, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Currency item) {
        helper.setText(R.id.tvLike, item.getSymbol());
        ((ImageView) helper.getView(R.id.ivLike)).setBackground(item.isCollect() ? mContext.getResources().getDrawable(R.drawable.icon_favority)
                : mContext.getResources().getDrawable(R.drawable.icon_unfavority));
        helper.getView(R.id.rlLike).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getExchangeable() == 1) {
                    if (item.isCollect()) {
                        item.setCollect(false);
                        ((ImageView) helper.getView(R.id.ivLike)).setBackground(mContext.getResources().getDrawable(R.drawable.icon_unfavority));
                    } else {
                        item.setCollect(true);
                        ((ImageView) helper.getView(R.id.ivLike)).setBackground(mContext.getResources().getDrawable(R.drawable.icon_favority));
                    }
                    onclickListenerCollect.collect(item);
                } else {
                    WonderfulToastUtils.showToast("该交易对暂未开放");
                }
            }
        });
    }

    OnclickListenerCollect onclickListenerCollect;

    public void setOnCollectListener(OnclickListenerCollect collect) {
        this.onclickListenerCollect = collect;
    }

    public interface OnclickListenerCollect {
        void collect(Currency item);
    }
}
