package com.bibi.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

import com.bibi.R;
import com.bibi.entity.Coin;
import com.bibi.entity.FiatAssetBean;
import com.bibi.ui.main.MainActivity;
import com.bibi.utils.WonderfulMathUtils;
import com.bibi.utils.WonderfulStringUtils;
import com.bibi.utils.WonderfulToastUtils;

/**
 * Created by Administrator on 2018/2/5.
 */

public class FiatAssetAdapter extends BaseQuickAdapter<FiatAssetBean, BaseViewHolder> {

    boolean hideZero=false;
    String searchKey="";
    int itemHeight=-100;
    boolean hideAmount=false;
    public FiatAssetAdapter(@LayoutRes int layoutResId, @Nullable List<FiatAssetBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, FiatAssetBean item) {
        if(itemHeight==-100){
            itemHeight=  helper.getView(R.id.ll_asset_container).getLayoutParams().height;
        }
        boolean show = true;
        if (hideZero && item.getBalance() == 0) {
            //如果选择了隐藏余额为0的币种，且此币种余额为0，就隐藏此item
            show = false;
        } else if (!TextUtils.isEmpty(searchKey) && !item.getCoin().getUnit().contains(searchKey)) {
            //如果有筛选条件key，且此币种名不包含key，就隐藏此item
            show=false;
        }
        ViewGroup.LayoutParams lp = helper.getView(R.id.ll_asset_container).getLayoutParams();
        lp.height= show?itemHeight:0;
        helper.getView(R.id.ll_asset_container).setLayoutParams(lp);
        helper.setText(R.id.tvCoinUnit, item.getCoin().getUnit())
                .setText(R.id.tvCanUse, hideAmount? WonderfulStringUtils.getString(R.string.text_hide_amount):WonderfulMathUtils.getRundNumber(Double.valueOf(new BigDecimal(item.getBalance()).toPlainString()),8,null) + "")
                .setText(R.id.tv_suocang, hideAmount?WonderfulStringUtils.getString(R.string.text_hide_amount):WonderfulMathUtils.getRundNumber(Double.valueOf(new BigDecimal((item.getFrozenBalance()+item.getBalance())*item.getCoin().getUsdRate()* MainActivity.rate).toPlainString()), 1, null) + "")
                .setText(R.id.tvFrozon, hideAmount?WonderfulStringUtils.getString(R.string.text_hide_amount):WonderfulMathUtils.getRundNumber(Double.valueOf(new BigDecimal(item.getFrozenBalance()).toPlainString()),8,null) + "")
                 ;
    }

    public void setHideZero(boolean hide){
        hideZero=hide;
        notifyDataSetChanged();
    }

    public void setSearchKey(String key){
        searchKey=key;
        notifyDataSetChanged();
    }

    public void setHideAmount(boolean hideAmount) {
        this.hideAmount = hideAmount;
    }
}
