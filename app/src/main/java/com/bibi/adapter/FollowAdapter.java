package com.bibi.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.DecimalFormat;
import java.util.List;

import com.bibi.R;
import com.bibi.customview.CircleImageView;
import com.bibi.entity.Currency;
import com.bibi.entity.Follow;
import com.bibi.entity.NiurenEntity;
import com.bibi.utils.WonderfulStringUtils;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/12
 */
public class FollowAdapter extends BaseQuickAdapter<NiurenEntity, BaseViewHolder> {
    private int position = 1;
    private boolean isLoad;

    public boolean isLoad() {
        return isLoad;
    }

    public void setLoad(boolean load) {
        isLoad = load;
    }

    public FollowAdapter(@Nullable List<NiurenEntity> data) {
        super(R.layout.adapter_follow_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final NiurenEntity item) {
        helper.setText(R.id.tvindex, position + ""
        );
//        helper.setText(R.id.tvindex, item.getRank() + ""
//        );
        helper.setText(R.id.tvUserName, item.getUserName());
        helper.setText(R.id.tvProfit, new DecimalFormat("#0.00").format(item.getProfit()));
        helper.getView(R.id.tvFollow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.select(item.getMemberId() + "");
            }
        });
        if (item.getAvatar() == null || WonderfulStringUtils.isEmpty(item.getAvatar()))
            Glide.with(mContext).load(R.mipmap.icon_default_header).centerCrop().into((CircleImageView) helper.getView(R.id.ivHeader));
        else
            Glide.with(mContext).load(item.getAvatar()).centerCrop().into((CircleImageView) helper.getView(R.id.ivHeader));
        position++;
    }

    public void clearPosition() {
        position = 1;
    }

    OnclickListenerSelect select;

    public void setOnSelectListener(OnclickListenerSelect select) {
        this.select = select;
    }

    public interface OnclickListenerSelect {

        void select(String memberId);
    }
}
