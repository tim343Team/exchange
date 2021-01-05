package com.bibi.ui.dialog;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import com.bibi.R;
import com.bibi.adapter.DialogCoinInfoAdapter;
import com.bibi.adapter.SimpleAdapter;
import com.bibi.base.BaseDialogFragment;
import com.bibi.entity.CoinInfo;
import com.bibi.entity.SimpleListBean;
import com.bibi.utils.WonderfulCommonUtils;
import com.bibi.utils.WonderfulDpPxUtils;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/30
 */
public class ListDialogFragment extends BaseDialogFragment {
    @BindView(R.id.llContent)
    LinearLayout llContent;
    @BindView(R.id.rvCoinList)
    RecyclerView recyclerView;

    private SimpleListBean bean;
    private SimpleAdapter adapter;

    @OnClick(R.id.tvCancle)
    public void back(){
        dismiss();
    }

    public static ListDialogFragment getInstance(SimpleListBean bean) {
        ListDialogFragment fragment = new ListDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", bean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_fragment_list;
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
                window.setLayout(llContent.getWidth(), llContent.getHeight() + WonderfulDpPxUtils.dip2px(getActivity(), 0));
            }
        });
    }

    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        bean = (SimpleListBean) bundle.getSerializable("bean");
        if (bean.getNewsItems() == null || bean.getNewsItems().size() == 0) {
            return;
        }
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new SimpleAdapter(R.layout.adapter_dialog_coininfo, bean.getNewsItems());
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (callback != null) {
                    for (int i = 0; i < bean.getNewsItems().size(); i++) {
                        if (i == position) {
                            bean.getNewsItems().get(i).setSelected(true);
                        } else {
                            bean.getNewsItems().get(i).setSelected(false);
                        }
                    }
                    callback.ItemClick(position);
                    dismiss();
                }
            }
        });
        recyclerView.setAdapter(adapter);
        rootView.post(new Runnable() {
            @Override
            public void run() {
                int height = 0;
                if (ImmersionBar.hasNavigationBar(getActivity()))
                    height = WonderfulCommonUtils.getStatusBarHeight(getActivity());
                window.setLayout(llContent.getWidth(), llContent.getHeight() + WonderfulDpPxUtils.dip2px(getActivity(), 0));
            }
        });
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
        void ItemClick(int position);
    }

}
