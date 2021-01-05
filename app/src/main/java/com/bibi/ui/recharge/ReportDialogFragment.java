package com.bibi.ui.recharge;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import com.bibi.R;
import com.bibi.app.MyApplication;
import com.bibi.base.BaseDialogFragment;
import com.bibi.utils.WonderfulCommonUtils;
import com.bibi.utils.WonderfulDpPxUtils;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/19
 */
public class ReportDialogFragment extends BaseDialogFragment {
    @BindView(R.id.tvCancle)
    TextView tvCancle;
    @BindView(R.id.tvERC20)
    TextView tvERC20;
    @BindView(R.id.tvOmni)
    TextView tvOmni;
    @BindView(R.id.llContent)
    LinearLayout llContent;

    public static ReportDialogFragment getInstance() {
        ReportDialogFragment fragment = new ReportDialogFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (!(getActivity() instanceof RechargeDialogFragment.OperateCallback)) {
//            throw new RuntimeException("The Activity which fragment is located must implement the OperateCallback interface!");
//        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_fragment_report;
    }

    @Override
    protected void initLayout() {
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.bottomDialog);
//        rootView.post(new Runnable() {
//            @Override
//            public void run() {
//                int height = 0;
//                if (ImmersionBar.hasNavigationBar(getActivity()))
//                    height = WonderfulCommonUtils.getStatusBarHeight(getActivity());
//                Log.e("TAGGGGGGGG:",llContent.getHeight()+"");
//                window.setLayout(MyApplication.getApp().getmWidth(), llContent.getHeight());
//            }
//        });
        window.setLayout(MyApplication.getApp().getmWidth(), WonderfulDpPxUtils.dip2px(getActivity(), 220));
    }

    @Override
    protected void initView() {
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvERC20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.toERC20();
                dismiss();
            }
        });
        tvOmni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.toOmni();
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

    OperateCallback callback;

    public void setOperateCallback(OperateCallback callback) {
        this.callback = callback;
    }

    public interface OperateCallback {
        void toERC20();
        void toOmni();
    }
}
