package com.bibi.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import com.bibi.R;
import com.bibi.base.LinAdapter;
import com.bibi.entity.AnnounceEntity;
import com.bibi.entity.TiBiAddressContent;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/4/8
 */
public class AnnouncementAdapter extends BaseQuickAdapter<AnnounceEntity, BaseViewHolder> {
    private String type;

    public AnnouncementAdapter(int layoutResId, @Nullable List<AnnounceEntity> data, String type) {
        super(layoutResId, data);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, final AnnounceEntity item) {
        if (item.getTitle().length()>15){
            String str;
            str=item.getTitle();
            str=str.substring(0,15);
            helper.setText(R.id.text_biaoti,str+"...");
        }else {
            helper.setText(R.id.text_biaoti,item.getTitle());
        }

        helper.setText(R.id.text_time,item.getCreateTime());
        if ("0".equals(item.getIsTop())){
            helper.getView(R.id.img_ding).setVisibility(View.VISIBLE);
        }else {
            helper.getView(R.id.img_ding).setVisibility(View.GONE);
        }
        helper.getView(R.id.llContent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detail.onDetail(item);
            }
        });
    }

    OnclickListenerDetail detail;

    public void setOnDetailListener(OnclickListenerDetail detail) {
        this.detail = detail;
    }

    public interface OnclickListenerDetail {

        void onDetail(AnnounceEntity item);
    }
}
