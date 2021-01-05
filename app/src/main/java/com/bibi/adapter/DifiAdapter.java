package com.bibi.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bibi.R;
import com.bibi.entity.Currency;
import com.bibi.entity.DifiBean;
import com.bibi.utils.WonderfulMathUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/9/23
 */
public class DifiAdapter extends BaseQuickAdapter<Currency, BaseViewHolder> {
    public DifiAdapter(int layoutResId, @Nullable List<Currency> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final Currency item) {
        Glide.with(mContext).load(item.getIcon()).placeholder(R.drawable.icon_bite)
                .into((ImageView) helper.getView(R.id.ivIcon));
        helper.setText(R.id.tvName, item.getSymbol());
        String format = new DecimalFormat("#0.00000000").format(item.getClose());
        BigDecimal bg = new BigDecimal(format);
        String v = bg.setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
        helper.setText(R.id.tvClose, v);
        helper.setText(R.id.item_home_chg, (item.getChg() >= 0 ? "+" : "") + WonderfulMathUtils.getRundNumber(item.getChg() * 100, 2, "########0.") + "%");
        helper.getView(R.id.item_home_chg).setEnabled(item.getChg() >= 0);
        helper.setText(R.id.kDataText, "￥" + item.getCnyPrice());
        helper.getView(R.id.tvKline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerKline.onStart(item.getSymbol(),item.getExchangeable());
            }
        });
    }

    OnclickListenerKline listenerKline;

    public void setOnKlineListener(OnclickListenerKline listenerKline) {
        this.listenerKline = listenerKline;
    }

    public interface OnclickListenerKline {

        void onStart(String symbol,int exchangeable);
    }

}
