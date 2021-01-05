package com.bibi.adapter;

import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import com.bibi.R;
import com.bibi.entity.Currency;
import com.bibi.entity.HoldEntity;

import static com.bibi.ui.order.OrdersFragment.HOLD;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/7/28
 */
public class CoinHoldAdapter extends BaseQuickAdapter<HoldEntity, BaseViewHolder> {
    private String type;
    private List<Currency> currencies;

    public CoinHoldAdapter(int layoutResId, @Nullable List<HoldEntity> data, String type, List<Currency> currencies) {
        super(layoutResId, data);
        this.type = type;
        this.currencies = currencies;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }

    @Override
    protected void convert(BaseViewHolder helper, final HoldEntity item) {
        if (type.equals(HOLD)) {
            helper.getView(R.id.llFinish).setVisibility(View.GONE);
            helper.getView(R.id.tvCancle).setVisibility(View.VISIBLE);

            if (item.getDirection().equals("BUY")) {
                helper.setTextColor(R.id.tvDirection, mContext.getResources().getColor(R.color.color_00b274));
            } else {
                helper.setTextColor(R.id.tvDirection, mContext.getResources().getColor(R.color.color_f15057));
            }
            helper.setText(R.id.tvType, item.getType().equals("LIMIT_PRICE") ? mContext.getResources().getString(R.string.LIMIT_PRICE) : mContext.getResources().getString(R.string.MARKET_PRICE));
            helper.setText(R.id.tvDirection, item.getDirection().equals("BUY") ? mContext.getResources().getString(R.string.text_buy) : mContext.getResources().getString(R.string.text_sale));
            helper.setText(R.id.tvSymbol, item.getSymbol());
            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String time = simpleDate.format(item.getTime());
            helper.setText(R.id.tvTime, time);
            Glide.with(mContext).load(item.getCoinIcon()).placeholder(R.drawable.icon_bite)
                    .into((ImageView) helper.getView(R.id.item_home_img));
            helper.setText(R.id.tvPrice, item.getPrice() + "");
            helper.setText(R.id.tvAmount, new DecimalFormat("#0.00").format(item.getAmount()) + "");
            helper.setText(R.id.tvTurnover, new DecimalFormat("#0.0000").format(item.getTurnover()) + "");
            helper.setText(R.id.tvFinish, item.getTradedAmount()+"");
            helper.getView(R.id.tvCancle).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onclickListenerColse.close(item.getOrderId());
                }
            });

        } else {
            helper.getView(R.id.llFinish).setVisibility(View.VISIBLE);
            helper.getView(R.id.tvCancle).setVisibility(View.GONE);
            if (item.getDirection().equals("BUY")) {
                helper.setTextColor(R.id.tvDirection, mContext.getResources().getColor(R.color.color_00b274));
            } else {
                helper.setTextColor(R.id.tvDirection, mContext.getResources().getColor(R.color.color_f15057));
            }
            helper.setText(R.id.tvType, item.getType().equals("LIMIT_PRICE") ? mContext.getResources().getString(R.string.LIMIT_PRICE) : mContext.getResources().getString(R.string.MARKET_PRICE));
            helper.setText(R.id.tvCurrentPrice,item.getCurrentPrice() + "");
            helper.setText(R.id.tvDirection, item.getDirection().equals("BUY") ? mContext.getResources().getString(R.string.text_buy) : mContext.getResources().getString(R.string.text_sale));
            helper.setText(R.id.tvSymbol, item.getSymbol());
            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String time = simpleDate.format(item.getTime());
            helper.setText(R.id.tvTime, time);
            Glide.with(mContext).load(item.getCoinIcon()).placeholder(R.drawable.icon_bite)
                    .into((ImageView) helper.getView(R.id.item_home_img));
            helper.setText(R.id.tvPrice, item.getPrice() + "");
            helper.setText(R.id.tvAmount, new DecimalFormat("#0.00").format(item.getAmount()) + "");
            helper.setText(R.id.tvTurnover, new DecimalFormat("#0.0000").format(item.getTurnover()) + "");
            helper.setText(R.id.tvFinish, item.getTradedAmount()+"");
            helper.setText(R.id.tvFee, item.getOrderFee()+"");
            helper.setText(R.id.tvStatus, item.getStatus().equals("CANCELED")?mContext.getResources().getString(R.string.cancelled):mContext.getResources().getString(R.string.order_done));
        }
    }

    OnclickListenerColse onclickListenerColse;

    public void setOnColseListener(OnclickListenerColse close) {
        this.onclickListenerColse = close;
    }

    public interface OnclickListenerColse {
        void close(String orderId);
    }
}
