package com.bibi.ui.my_promotion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import com.bibi.R;
import com.bibi.base.BaseActivity;
import com.bibi.ui.order_detail.OrderDetailReleaseDialog;
import com.bibi.utils.WonderfulStringUtils;
import com.bibi.utils.WonderfulToastUtils;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/17
 */
public class PromotionOutActivity extends BaseActivity {
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;
    @BindView(R.id.etPassword)
    EditText etPassword;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PromotionOutActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_promotion_out;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (WonderfulStringUtils.isEmpty(etPassword.getText().toString())) {
//                    WonderfulToastUtils.showToast(getResources().getString(R.string.paymentTip6));
//                    return;
//                }
//                presenter.release(getToken(), orderSn, etPassword.getText().toString());
            }
        });
    }

    @Override
    protected void obtainData() {

    }

    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(this, llTitle);
            isSetTitle = true;
        }
    }
}
