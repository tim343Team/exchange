package com.bibi.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.bibi.R;
import com.bibi.customview.CircleImageView;
import com.bibi.entity.C2C;
import com.bibi.utils.WonderfulMathUtils;
import com.bibi.utils.WonderfulStringUtils;
import com.bibi.utils.WonderfulToastUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2018/2/28.
 */

public class C2CAdapter extends BaseQuickAdapter<C2C.C2CBean, BaseViewHolder> {
    private Context context;
    private String priceType="CNY";

    public C2CAdapter(Context context,String priceType, int layoutResId, @Nullable List<C2C.C2CBean> data) {
        super(layoutResId, data);
        this.context = context;
        this.priceType = priceType;
    }
    @Override
    protected void convert(BaseViewHolder helper, C2C.C2CBean item) {
//        String name=item.getMemberName();
//        name=name.substring(0,name.length()-2);

        helper.setText(R.id.tvName, item.getMemberName()).setText(R.id.tvLimit,mContext.getResources().getString(R.string.text_quota)+" "+WonderfulStringUtils.getThousand(String.valueOf(item.getMinLimit())) + "~" + WonderfulStringUtils.getThousand(String.valueOf(item.getMaxLimit())) + " CNY")
                .setText(R.id.tvBuy, ("0".equals(item.getAdvertiseType()) ?
                mContext.getResources().getString(R.string.text_chu_sho) : mContext.getResources().getString(R.string.text_gou_mai)))
                .setText(R.id.tvTradeAmount, "成交量： "+item.getTransactions());
        if(priceType.equals("CNY")){
//            helper.setText(R.id.tvPrice,"¥"+ WonderfulMathUtils.getRundNumber(item.getPrice(),2,null) );

            helper.setText(R.id.tvPrice,"¥"+ new DecimalFormat("#0.00").format(item.getPrice()));
            helper.setText(R.id.tvPriceType,"/USDT");
        }else {
//            helper.setText(R.id.tvPrice,"$"+ WonderfulMathUtils.getRundNumber(item.getPrice(),2,null) );
            helper.setText(R.id.tvPrice,"$"+ new DecimalFormat("#0.00").format(item.getPrice()));
            helper.setText(R.id.tvPriceType,"/USDT");
        }
        helper.setBackgroundRes(R.id.tvBuy,("0".equals(item.getAdvertiseType()) ?
                R.drawable.shape_c2c_tv_sell : R.drawable.shape_c2c_tv_buy));
        helper.setText(R.id.tvNumber,mContext.getResources().getString(R.string.amount)+" "+new BigDecimal(item.getRemainAmount()).setScale(2,BigDecimal.ROUND_DOWN).toPlainString());
        //改变为头像
        if (item.getAvatar() == null || WonderfulStringUtils.isEmpty(item.getAvatar()))
            Glide.with(mContext).load(R.mipmap.icon_default_header).centerCrop().into((CircleImageView) helper.getView(R.id.tv_header));
        else
            Glide.with(mContext).load(item.getAvatar()).centerCrop().into((CircleImageView) helper.getView(R.id.tv_header));

        List<String> pays = Arrays.asList(item.getPayMode().split(","));
        if (pays.contains("支付宝")) helper.setVisible(R.id.ivZhifubao, true);
        else helper.setGone(R.id.ivZhifubao, false);
        if (pays.contains("微信")) helper.setVisible(R.id.ivWeChat, true);
        else helper.setGone(R.id.ivWeChat, false);
        if (pays.contains("银联") || pays.contains("银行卡")) helper.setVisible(R.id.ivUnionPay, true);
        else helper.setGone(R.id.ivUnionPay, false);
        if (pays.contains("SWIFT") ) helper.setVisible(R.id.ivGuoji, true);
        else helper.setGone(R.id.ivGuoji, false);
    }
}
