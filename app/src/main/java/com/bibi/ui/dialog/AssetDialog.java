package com.bibi.ui.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
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
 * @version 1.0 2020/7/31
 */
public class AssetDialog extends BaseDialogFragment {
    @BindView(R.id.llContent)
    LinearLayout llContent;
    @BindView(R.id.llTab)
    LinearLayout llTab;
    @BindView(R.id.tvChargeMoney)
    TextView tvChargeMoney;
    @BindView(R.id.tvWithdraw)
    TextView tvWithdraw;
    @BindView(R.id.tvTran)
    TextView tvTran;

    @OnClick(R.id.tvChargeMoney)
    public void chargeMoney() {
        callback.ItemClick(0);
        dismiss();
    }

    @OnClick(R.id.tvWithdraw)
    public void withdraw() {
        callback.ItemClick(1);
        dismiss();
    }

    @OnClick(R.id.tvTran)
    public void tran() {
        callback.ItemClick(2);
        dismiss();
    }

    public static AssetDialog newInstance(int canRecharge, int canWithdraw, int canTransfer, String type) {
        AssetDialog fragment = new AssetDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("canRecharge", canRecharge);
        bundle.putInt("canWithdraw", canWithdraw);
        bundle.putInt("canTransfer", canTransfer);
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_asset;
    }

    @Override
    protected void initLayout() {
        int canRecharge = getArguments().getInt("canRecharge");
        int canWithdraw = getArguments().getInt("canWithdraw");
        int canTransfer = getArguments().getInt("canTransfer");
        String type = getArguments().getString("type");
        if (type.equals("OPTIONS")) {
            if (canRecharge == 0) {
                tvChargeMoney.setEnabled(false);
                tvChargeMoney.setTextColor(getResources().getColor(R.color.primaryTextGray));
            } else {
                tvChargeMoney.setEnabled(true);
                tvChargeMoney.setTextColor(getResources().getColor(R.color.primaryText));
            }
            if (canWithdraw == 0) {
                tvWithdraw.setEnabled(false);
                tvWithdraw.setTextColor(getResources().getColor(R.color.primaryTextGray));
            } else {
                tvWithdraw.setEnabled(true);
                tvWithdraw.setTextColor(getResources().getColor(R.color.primaryText));
            }
            if (canTransfer == 0) {
                tvTran.setEnabled(false);
                tvTran.setTextColor(getResources().getColor(R.color.primaryTextGray));
            } else {
                tvTran.setEnabled(true);
                tvTran.setTextColor(getResources().getColor(R.color.primaryText));
            }
            llTab.setVisibility(View.VISIBLE);
        } else {
            llTab.setVisibility(View.GONE);
        }
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.bottomDialog);
        rootView.post(new Runnable() {
            @Override
            public void run() {
                int height = 0;
                if (ImmersionBar.hasNavigationBar(getActivity()))
                    height = WonderfulCommonUtils.getStatusBarHeight(getActivity());
                window.setLayout(llContent.getWidth(), llContent.getHeight() + 40 + WonderfulDpPxUtils.dip2px(getActivity(), height / 2));

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
