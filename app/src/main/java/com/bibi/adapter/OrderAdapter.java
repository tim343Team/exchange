package com.bibi.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.bibi.R;
import com.bibi.entity.Order;
import com.bibi.ui.my_order.OrderFragment;
import com.bibi.utils.WonderfulLogUtils;
import com.bibi.utils.WonderfulToastUtils;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2018/2/5.
 */

public class OrderAdapter extends BaseQuickAdapter<Order, BaseViewHolder> {

    private Context context;
    private int status;

    public OrderAdapter(Context context, @LayoutRes int layoutResId, @Nullable List<Order> data,int status) {
        super(layoutResId, data);
        this.context = context;
        this.status = status;
    }

    @Override
    protected void convert(BaseViewHolder helper, Order item) {
        String name = item.getRealName();
        if (name.length() > 1) {
            name = name.substring(0, 1);
        }
        helper.setText(R.id.tvName, name + "**").setText(R.id.tvCount, item.getAmount() + item.getUnit()).setText(R.id.tvTotal, item.getMoney() + "CNY");
        if (status==1){
            helper.setText(R.id.tvType, "等待付款");
        }else if(status==2){
            helper.setText(R.id.tvType,"等待放币");
        }else if(status==3){
            helper.setText(R.id.tvType, "交易完成");
        }else if(status==0){
            helper.setText(R.id.tvType, "订单");
        }else if(status==4){
            helper.setText(R.id.tvType, "申诉中");
        }
//                .setBackgroundColor(R.id.tvType, item.getType() == 0 ? mContext.getResources().getColor(R.color.typeGreen) : mContext.getResources().getColor(R.color.typeRed))
        if (item.getAvatar() == null || "".equals(item.getAvatar())) {
            Glide.with(context).load(R.mipmap.icon_default_header)
                    .diskCacheStrategy(DiskCacheStrategy.NONE).into((ImageView) helper.getView(R.id.ivHeader));
        } else {
            Glide.with(context).load(item.getAvatar())
                    .diskCacheStrategy(DiskCacheStrategy.NONE).into((ImageView) helper.getView(R.id.ivHeader));
        }
    }
}
