package com.bibi.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.bibi.R;
import com.bibi.app.MyApplication;
import com.bibi.entity.PromotionRecord;
import java.util.List;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public class PromotionRecordAdapter extends BaseQuickAdapter<PromotionRecord, BaseViewHolder> {
    private int type = 0;

    public PromotionRecordAdapter(int layoutResId, @Nullable List<PromotionRecord> data) {
        super(layoutResId, data);
    }

    public PromotionRecordAdapter(int layoutResId, @Nullable List<PromotionRecord> data, int type) {
        super(layoutResId, data);
        this.type = type;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final PromotionRecord item) {
        if(type==0){
            helper.setText(R.id.tvTab3,mContext.getResources().getString(R.string.direct_number));
            helper.getView(R.id.tvTab4).setVisibility(View.GONE);
            helper.getView(R.id.tvRegistrationTime).setVisibility(View.GONE);
        }else {
            helper.setText(R.id.tvTab3,mContext.getResources().getString(R.string.next_number));
            helper.getView(R.id.tvTab4).setVisibility(View.GONE);
            helper.getView(R.id.tvRegistrationTime).setVisibility(View.GONE);
        }
        int primaryText = ContextCompat.getColor(MyApplication.getApp(), R.color.primaryText);
        int blue = ContextCompat.getColor(MyApplication.getApp(), R.color.timeCome1);
        if (item.isSelect()) {
            helper.getView(R.id.llMore).setVisibility(View.VISIBLE);
        } else {
            helper.getView(R.id.llMore).setVisibility(View.GONE);
        }
        helper.setText(R.id.tvUserName, item.getMobilePhone() + "");
        helper.setText(R.id.RecommendationLevel, item.getGrade() + "");
        helper.setText(R.id.tvCount, item.getInviteCount() + "");
        helper.setTextColor(R.id.tvCount, item.getInviteCount() > 0 ? blue : primaryText);
        helper.setText(R.id.tvRegistrationTime, item.getCreateTime() + "");
        if (item.getInviteCount() > 0) {
            helper.getView(R.id.llRoot).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //没有被选中展开才能展开
                    if (!item.isSelect()) {
                        spread.select(item, helper.getAdapterPosition());
                    }
                }
            });
        }

        helper.getView(R.id.llMore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //被选中展开的才能收拢
                if (item.isSelect()) {
                    //收拢的层级
                    merge.select(item, 0);
                }
            }
        });
    }

    OnclickSpread spread;

    public void OnclickSpread(OnclickSpread spread) {
        this.spread = spread;
    }

    public interface OnclickSpread {

        void select(PromotionRecord item, int position);
    }

    OnclickMerge merge;

    public void OnclickMerge(OnclickMerge merge) {
        this.merge = merge;
    }

    public interface OnclickMerge {

        void select(PromotionRecord item, int position);
    }


}
