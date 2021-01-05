package com.bibi.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.bibi.R;
import com.bibi.ui.main.MainActivity;
import com.bibi.app.MyApplication;
import com.bibi.entity.Currency;
import com.bibi.utils.WonderfulMathUtils;
import com.bibi.utils.WonderfulToastUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;


public class Homes2Adapter extends BaseQuickAdapter<Currency, BaseViewHolder> {
    private int type;
    private boolean isLoad;

    public boolean isLoad() {
        return isLoad;
    }

    public void setLoad(boolean load) {
        isLoad = load;
    }

    public Homes2Adapter(@Nullable List<Currency> data, int type) {
//        super(R.layout.adapter_layout_two2, data);
        super(R.layout.adapter_home_layout, data);
        this.type = type;
    }


    @Override
    protected void convert(BaseViewHolder helper, Currency item) {
//        if (type == 2) {
//            helper.setText(R.id.item_home_symbol, item.getSymbol().split("/")[0]);
//            helper.setText(R.id.item_home_symbolTwo, "/" + item.getSymbol().split("/")[1]);
//        } else {
//            helper.setText(R.id.item_home_symbol, item.getOtherCoin());
//            helper.setText(R.id.item_home_symbolTwo, item.getSymbol().substring(item.getSymbol().indexOf("/"), item.getSymbol().length()));
//        }
//        String format = new DecimalFormat("#0.00000000").format(item.getClose());
//        BigDecimal bg = new BigDecimal(format);
//        String v =  bg.setScale(8,BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
//        helper.setText(R.id.item_home_close,v);
//        helper.setTextColor(R.id.item_home_close, item.getChg() >= 0 ? ContextCompat.getColor(MyApplication.getApp(),
//                R.color.typeGreen) : ContextCompat.getColor(MyApplication.getApp(), R.color.typeRed));
        if (item.getChg() >= 0) {
            helper.getView(R.id.imgChg).setBackgroundResource(R.drawable.icon_rise);
            ((TextView) helper.getView(R.id.item_home_close)).setTextColor(mContext.getResources().getColor(R.color.color_00b274));
        } else {
            helper.getView(R.id.imgChg).setBackgroundResource(R.drawable.icon_fall);
            ((TextView) helper.getView(R.id.item_home_close)).setTextColor(mContext.getResources().getColor(R.color.typeRed));
        }
        Glide.with(mContext).load(item.getIcon()).placeholder(R.drawable.icon_bite)
                .into((ImageView) helper.getView(R.id.item_home_img));
        helper.setText(R.id.item_home_chg, (item.getChg() >= 0 ? "+" : "") + WonderfulMathUtils.getRundNumber(item.getChg() * 100, 2, "########0.") + "%");
        helper.getView(R.id.item_home_chg).setEnabled(item.getChg() >= 0);
        String symbol = item.getSymbol();
        String[] coinNames = symbol.split("/");
        helper.setText(R.id.item_home_coin, coinNames[0]);
//        helper.setText(R.id.item_home_coin_base, "/" + coinNames[1]);
        String format = new DecimalFormat("#0.00000000").format(item.getClose());
        BigDecimal bg = new BigDecimal(format);
        String v = bg.setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
        helper.setText(R.id.item_home_close, v);
        helper.setText(R.id.kDataText, "≈ ￥" + item.getCnyPrice());
    }


}