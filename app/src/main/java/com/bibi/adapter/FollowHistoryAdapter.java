package com.bibi.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import com.bibi.R;
import com.bibi.entity.Address;
import com.bibi.entity.FollowHistoryContent;
import com.bibi.entity.FollowHistoryEntity;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/22
 */
public class FollowHistoryAdapter extends BaseQuickAdapter<FollowHistoryContent, BaseViewHolder> {

    public FollowHistoryAdapter(int layoutResId, @Nullable List<FollowHistoryContent> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final FollowHistoryContent item) {
        helper.setText(R.id.tvType,item.getSymbol());
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String time = simpleDate.format(item.getCreateTime());
        helper.setText(R.id.tvTime, time);
//        helper.setText(R.id.tvTime,item.getCreateTime()+"");
        helper.setText(R.id.tvProfit,item.getProfit());
        helper.setText(R.id.tvUserName,item.getFmember().getNickname());
        helper.setText(R.id.tvStatus,mContext.getResources().getString(R.string.auto));
        if(item.getStatus()==0){
            helper.setText(R.id.tvCancle,mContext.getResources().getString(R.string.cancelled));
            ((TextView) helper.getView(R.id.tvCancle)).setTextColor(mContext.getResources().getColor(R.color.primaryTextGray));
            ((TextView) helper.getView(R.id.tvCancle)).setBackgroundResource(R.drawable.circle_conner_rect_grat_13_back);
        }else {
            helper.getView(R.id.tvCancle).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete.onDelete(item.getId());
                }
            });
        }

    }

    OnclickListenerDelete delete;

    public void setOnDeleteListener(OnclickListenerDelete delete) {
        this.delete = delete;
    }

    public interface OnclickListenerDelete {

        void onDelete(long followId);
    }
}
