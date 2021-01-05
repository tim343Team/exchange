package com.bibi.ui.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.gyf.barlibrary.ImmersionBar;

import com.bibi.R;
import com.bibi.base.BaseDialogFragment;
import com.bibi.utils.WonderfulCommonUtils;
import com.bibi.utils.WonderfulDpPxUtils;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/8/13
 */
public class PasswordDialog extends BaseDialogFragment {
    @BindView(R.id.llContent)
    LinearLayout llContent;
    @BindView(R.id.tvPassword)
    EditText tvPassword;

    @OnClick(R.id.tvEnter)
    public void sure() {
        try {
            ((PositionSettingDialog.OperateCallback) getTargetFragment()).cancleOrder(tvPassword.getText().toString());
        } catch (Exception e) {
            if (callback != null)
                callback.cancleOrder(tvPassword.getText().toString());
        }
        dismiss();
    }

    public static PasswordDialog getInstance() {
        PasswordDialog fragment = new PasswordDialog();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_password;
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

    }

    @Override
    protected void loadData() {

    }

    private OperateCallback callback;

    public void setCallback(OperateCallback callback) {
        this.callback = callback;
    }

    public interface OperateCallback {
        void cancleOrder(String stopLossPrice);
    }
}
