package com.bibi.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import com.bibi.R;
import com.bibi.base.BaseDialogFragment;
import com.bibi.entity.EntrustHistory;
import com.bibi.utils.WonderfulCommonUtils;
import com.bibi.utils.WonderfulDpPxUtils;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/24
 */
public class ModifyProfitDialogFragment extends BaseDialogFragment {
    @BindView(R.id.llContent)
    LinearLayout llContent;
    @BindView(R.id.mProfitPrice)
    EditText mProfitPrice;
    @BindView(R.id.mLoosePrice)
    EditText mLoosePrice;

    String orderId;
    String stopProfitPrice;
    String stopLossPrice;

    @OnClick(R.id.tvCancle)
    public void cancle() {
        dismiss();
    }

    @OnClick(R.id.tvSureOrder)
    public void sure() {
        try {
            ((OperateCallback) getTargetFragment()).cancleOrder(orderId,mProfitPrice.getText().toString(), mLoosePrice.getText().toString());
        } catch (Exception e) {
            if (callback != null)
                callback.cancleOrder(orderId,mProfitPrice.getText().toString(), mLoosePrice.getText().toString());
        }
        dismiss();
    }

    public static ModifyProfitDialogFragment getInstance(String orderId, String stopProfitPrice, String stopLossPrice) {
        ModifyProfitDialogFragment fragment = new ModifyProfitDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("orderId", orderId);
        bundle.putString("stopProfitPrice", stopProfitPrice);
        bundle.putString("stopLossPrice", stopLossPrice);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_fragment_moddify;
    }

    @Override
    protected void initLayout() {
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.bottomDialog);
        rootView.post(new Runnable() {
            @Override
            public void run() {
                int height = 0;
                if (ImmersionBar.hasNavigationBar(getActivity()))
                    height = WonderfulCommonUtils.getStatusBarHeight(getActivity());
                window.setLayout(llContent.getWidth(), llContent.getHeight() + WonderfulDpPxUtils.dip2px(getActivity(), height/2));
            }
        });
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void fillWidget() {
        Bundle bundle = getArguments();
        orderId = bundle.getString("orderId");
        stopProfitPrice = bundle.getString("stopProfitPrice");
        stopLossPrice = bundle.getString("stopLossPrice");
        mProfitPrice.setText(stopProfitPrice);
        mLoosePrice.setText(stopLossPrice);
    }

    @Override
    protected void loadData() {

    }

    private OperateCallback callback;

    public void setCallback(OperateCallback callback) {
        this.callback = callback;
    }

    public interface OperateCallback {
        void cancleOrder(String orderId, String stopProfitPrice, String stopLossPrice);
    }
}
