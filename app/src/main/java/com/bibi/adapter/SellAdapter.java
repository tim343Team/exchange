package com.bibi.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.bibi.R;
import com.bibi.app.MyApplication;
import com.bibi.entity.Exchange;
import com.bibi.utils.WonderfulLogUtils;
import com.bibi.utils.WonderfulMathUtils;
import com.bibi.utils.WonderfulToastUtils;

import java.math.BigDecimal;
import java.util.List;



public class SellAdapter extends BaseQuickAdapter<Exchange, BaseViewHolder> {

    private int type = 0; // 为0就是绿色否则为红色
    private int price = 2;
    private int amount = 2;
    private myText text;
    float totalAmount = 0;

    public myText getText() {
        return text;
    }


    public void setText(myText text) {
        this.text = text;
    }

    public SellAdapter(@Nullable List<Exchange> data, int type) {
        super(R.layout.adapter_sell_layout, data);
        this.type = type;
        getTotalCount(data);
    }

    public void setList(List<Exchange> data) {
        this.mData = data;
        getTotalCount(data);
    }


    private void getTotalCount(List<Exchange> data) {
        totalAmount = 0;
        for (Exchange exchange : data) {
            if(exchange.getAmountStr()==null){
                exchange.setAmountStr("--");
            }
            if (!"--".equals(exchange.getAmountStr())) {
                totalAmount += Float.parseFloat(exchange.getAmountStr());
            }
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, Exchange item) {
        helper.setText(R.id.item_position,(helper.getAdapterPosition()+1)+"");
        // 对不同的币种做不同的限制
        if (text != null) {
            price = text.two();
            amount = text.one();
        }
        if ("--".equals(item.getPriceStr()) || "--".equals(item.getAmountStr())) {
            helper.setText(R.id.item_sell_two, String.valueOf(item.getPriceStr()));
            helper.setText(R.id.item_sell_three, String.valueOf(item.getAmountStr()));
        } else {
//            BigDecimal bg = new BigDecimal(WonderfulMathUtils.getRundNumber(Double.valueOf(item.getPriceStr()), price, null));
//            String v = bg.setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
            BigDecimal bg1 = new BigDecimal(WonderfulMathUtils.getRundNumber(Double.valueOf(item.getAmountStr()), amount, null));
            String v1 = bg1.setScale(8, BigDecimal.ROUND_DOWN).stripTrailingZeros().toPlainString();
            helper.setText(R.id.item_sell_two, "" + item.getPriceStr());
            helper.setText(R.id.item_sell_three, "" + v1);
        }
        int position = helper.getAdapterPosition();
        float currentTotalAmount = 0;
        if (type == 1) {
            for (int i = 0; i <= position; i++) {
                if(mData.get(i).getAmountStr()==null){
                    mData.get(i).setAmountStr("--");
                }
                if (!"--".equals(mData.get(i).getAmountStr())) {
                    currentTotalAmount += Float.parseFloat(mData.get(i).getAmountStr());
                }
            }
        } else {
            for (int i = mData.size() - 1; i >= position; i--) {
                if(mData.get(i).getAmountStr()==null){
                    mData.get(i).setAmountStr("--");
                }
                if (!"--".equals(mData.get(i).getAmountStr())) {
                    currentTotalAmount += Float.parseFloat(mData.get(i).getAmountStr());
                }
            }
        }
        float scale = 0;
        if (totalAmount != 0) {
            scale = currentTotalAmount / totalAmount;
        }
        //Log.i("getAdapterPosition",position+"--"+currentTotalAmount+"--"+totalAmount+"--"+scale);
        scale = scale > 1 ? 1 : scale;
        int parentWidth=helper.getView(R.id.ceshi).getMeasuredWidth();
        int backWidth = (int) (parentWidth* scale);
        ViewGroup.LayoutParams lp = helper.getView(R.id.tv_back_depth).getLayoutParams();
        int lastPosition = type == 1 ? mData.size() - 1 : 0;
        if(mData.get(position).getAmountStr()==null){
            mData.get(position).setAmountStr("--");
        }
        if (!"--".equals(mData.get(position).getAmountStr())) {
            if (position == lastPosition) {
                lp.width = parentWidth-1;
                helper.getView(R.id.tv_back_depth).setLayoutParams(lp);
            } else {
                lp.width = backWidth>=1?backWidth-1:backWidth;
                helper.getView(R.id.tv_back_depth).setLayoutParams(lp);
            }
        }else {
            lp.width = 0;
            helper.getView(R.id.tv_back_depth).setLayoutParams(lp);
        }


        if (type == 1) {
            helper.setTextColor(R.id.item_sell_two, ContextCompat.getColor(MyApplication.getApp(),
                    R.color.typeGreen));
            helper.setBackgroundColor(R.id.tv_back_depth, ContextCompat.getColor(MyApplication.getApp(),
                    R.color.green_back));
        }
    }

    public interface myText {
        int one();

        int two();
    }
}
