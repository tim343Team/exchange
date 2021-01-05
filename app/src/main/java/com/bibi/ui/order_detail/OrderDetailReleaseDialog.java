package com.bibi.ui.order_detail;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import com.bibi.R;
import com.bibi.app.MyApplication;
import com.bibi.base.BaseDialogFragment;
import com.bibi.utils.WonderfulCommonUtils;
import com.bibi.utils.WonderfulDpPxUtils;
import com.bibi.utils.WonderfulToastUtils;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/13
 */
public class OrderDetailReleaseDialog extends BaseDialogFragment {
    @BindView(R.id.llContent)
    LinearLayout llContent;
    @BindView(R.id.tvCancle)
    TextView tvCancle;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.checkbox)
    CheckBox checkbox;

    public static OrderDetailReleaseDialog getInstance() {
        OrderDetailReleaseDialog fragment = new OrderDetailReleaseDialog();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(getActivity() instanceof OperateCallback)) {
            throw new RuntimeException("The Activity which fragment is located must implement the OperateCallback interface!");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_fragment_order_release;
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
//        window.setLayout(MyApplication.getApp().getmWidth(), WonderfulDpPxUtils.dip2px(getActivity(), 340));

    }

    @Override
    protected void initView() {
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkbox.isChecked()){
                    WonderfulToastUtils.showToast(getResources().getString(R.string.payment_has_been));
                    return;
                }
                ((OperateCallback) getActivity()).release(etPassword.getText().toString());
                dismiss();
            }
        });
    }

    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {

    }

    public interface OperateCallback {
        void release(String jyPassword);
    }
}
