package com.bibi.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import com.bibi.R;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/4/3
 */
public class BannerViewHolder implements MZViewHolder<String> {
    private ImageView mImageView;
    @Override
    public View createView(Context context) {
        // 返回页面布局
        View view = LayoutInflater.from(context).inflate(R.layout.banner_item,null);
        mImageView = (ImageView) view.findViewById(R.id.banner_image);
        return view;
    }

    @Override
    public void onBind(Context context, int position, String data) {
        // 数据绑定
        Glide.with(context).load(data).asBitmap().centerCrop().into(mImageView);
//        mImageView.setImageResource(data);
    }
}
