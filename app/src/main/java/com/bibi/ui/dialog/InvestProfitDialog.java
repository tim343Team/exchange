package com.bibi.ui.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
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
 * @version 1.0 2020/8/12
 */
public class InvestProfitDialog  extends BaseDialogFragment {
    @BindView(R.id.llContent)
    LinearLayout llContent;
    @BindView(R.id.tvContent)
    TextView tvContent;

    String price;

    @OnClick(R.id.btFinish)
    void finish(){
        callback.ItemClick();
        dismiss();
    }


    public static InvestProfitDialog getInstance(String price) {
        InvestProfitDialog fragment = new InvestProfitDialog();
        Bundle bundle = new Bundle();
        bundle.putString("price", price);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_invest_profit;
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
        price = bundle.getString("price");
        String sInfoFormat = getResources().getString(R.string.profit_result_note);
        String sFinalInfo = String.format(sInfoFormat, price);
        tvContent.setText(sFinalInfo);
    }

    @Override
    protected void loadData() {

    }

    private OperateCallback callback;

    public void setCallback(OperateCallback callback) {
        this.callback = callback;
    }

    public interface OperateCallback {
        void ItemClick();
    }
}
