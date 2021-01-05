package com.bibi.ui.order_detail;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import com.bibi.R;
import com.bibi.app.MyApplication;
import com.bibi.base.BaseDialogFragment;
import com.bibi.ui.buy_or_sell.OrderConfirmDialogFragment;
import com.bibi.utils.WonderfulDpPxUtils;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/10
 */
public class OrderDetailFragment extends BaseDialogFragment {
    @BindView(R.id.tvCancle)
    TextView tvCancle;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;

    public static OrderDetailFragment getInstance() {
        OrderDetailFragment fragment = new OrderDetailFragment();
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
        return R.layout.dialog_fragment_order_detail;
    }

    @Override
    protected void initLayout() {
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.bottomDialog);
        window.setLayout(MyApplication.getApp().getmWidth(), WonderfulDpPxUtils.dip2px(getActivity(), 340));

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
                ((OperateCallback) getActivity()).admit();
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
        void admit();
    }
}
