package com.bibi.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import com.bibi.R;
import com.bibi.entity.CoinTypeEntity;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/23
 */
public class TypeRecyAdapter extends BaseQuickAdapter<CoinTypeEntity, BaseViewHolder> {
    private String symbolType="";

    public TypeRecyAdapter(int layoutResId, @Nullable List<CoinTypeEntity> data) {
        super(layoutResId, data);
        if (data.size() > 0)
            symbolType = data.get(0).getSymbol();
    }

    public void setSymbolType(String symbolType) {
        this.symbolType = symbolType;
    }

    public String getSymbolType() {
        return symbolType;
    }

    @Override
    protected void convert(BaseViewHolder helper, final CoinTypeEntity item) {
        String symbol = item.getSymbol();
        String[] coinNames = symbol.split("/");
        helper.setText(R.id.tvType, coinNames[0]);
        if (symbolType.equals(item.getSymbol())){
            ((TextView) helper.getView(R.id.tvType)).setTextColor(mContext.getResources().getColor(R.color.white));
            ((TextView) helper.getView(R.id.tvType)).setBackgroundResource(R.drawable.shape_selector_f5a623_background);
        }else {
            ((TextView) helper.getView(R.id.tvType)).setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            ((TextView) helper.getView(R.id.tvType)).setBackgroundResource(R.drawable.shape_unselector_background);
        }
        helper.getView(R.id.tvType).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                symbolType = item.getSymbol();
                select.select(symbolType);
            }
        });
    }

    OnclickListenerSelect select;

    public void setOnSelectListener(OnclickListenerSelect select) {
        this.select = select;
    }

    public interface OnclickListenerSelect {

        void select(String symbol);
    }
}
