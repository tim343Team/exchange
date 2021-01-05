package com.bibi.ui.setting;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import com.bibi.R;
import com.bibi.base.BaseActivity;
import com.bibi.data.DataSource;
import com.bibi.data.RemoteDataSource;
import com.bibi.utils.WonderfulStringUtils;

public class FeeSettingActivity extends BaseActivity {
    @BindView(R.id.tv_exchange_rate_maker)
    TextView tv_exchange_rate_maker;
    @BindView(R.id.tv_exchange_rate_taker)
    TextView tv_exchange_rate_taker;
    @BindView(R.id.tv_margin_rate)
    TextView tv_margin_rate;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_fee_setting;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void obtainData() {

    }

    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {
        RemoteDataSource.getInstance().getFeeRate(getToken(), new DataSource.DataCallback() {
            @Override
            public void onDataLoaded(Object obj) {
                List<Double> entity = (List<Double>) obj;
                tv_exchange_rate_maker.setText(WonderfulStringUtils.getLongFloatString(entity.get(0),6) );
                tv_exchange_rate_taker.setText(WonderfulStringUtils.getLongFloatString(entity.get(1),6));
                tv_margin_rate.setText(WonderfulStringUtils.getLongFloatString(entity.get(2),6));
            }

            @Override
            public void onDataNotAvailable(Integer code, String toastMessage) {

            }
        });
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(this, llTitle);
            isSetTitle = true;
        }
    }
    @OnClick(R.id.ibBack)
    public void onBackClick(){
        onBackPressed();
    }

}
