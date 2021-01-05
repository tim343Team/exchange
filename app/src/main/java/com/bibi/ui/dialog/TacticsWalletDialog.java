package com.bibi.ui.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
 * @version 1.0 2020/8/10
 */
public class TacticsWalletDialog  extends BaseDialogFragment{
    @BindView(R.id.llContent)
    LinearLayout llContent;
    @BindView(R.id.tvContent)
    TextView tvContent;

    String content;

    public static TacticsWalletDialog getInstance(String content) {
        TacticsWalletDialog fragment = new TacticsWalletDialog();
        Bundle bundle = new Bundle();
        bundle.putString("content", content);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_tactics_wallet;
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
        content = bundle.getString("content");
//        heightPrice = bundle.getString("heightPrice");
//        mLoosePrice.setHint(lowPrice+"起购");
        tvContent.setText(content);
    }

    @Override
    protected void loadData() {

    }
}
