package com.bibi.ui.dialog;

import android.os.Bundle;
import android.view.Gravity;
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
 * @version 1.0 2020/6/12
 */
public class AmountDialog extends BaseDialogFragment {
    @BindView(R.id.llContent)
    LinearLayout llContent;

    @OnClick(R.id.tvCancle)
    public void back() {
        dismiss();
    }

    @OnClick(R.id.tvContract)
    public void selectContract() {
        callback.ItemClick(0);
        dismiss();
    }

    @OnClick(R.id.tvOptions)
    public void selectOptions() {
        callback.ItemClick(1);
        dismiss();
    }


    public static AmountDialog newInstance() {
        AmountDialog fragment = new AmountDialog();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_amount;
    }

    @Override
    protected void initLayout() {
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.bottomDialog);
        rootView.post(new Runnable() {
            @Override
            public void run() {
                int height = 0;
                if (ImmersionBar.hasNavigationBar(getActivity()))
                    height = WonderfulCommonUtils.getStatusBarHeight(getActivity());
                window.setLayout(llContent.getWidth(), llContent.getHeight() + WonderfulDpPxUtils.dip2px(getActivity(), height / 2));

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
        void ItemClick(int type);
    }
}
