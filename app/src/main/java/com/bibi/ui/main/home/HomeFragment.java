package com.bibi.ui.main.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bibi.R;
import com.bibi.adapter.HomeAdapter;
import com.bibi.base.BaseLazyFragment;
import com.bibi.entity.Currency;
import com.bibi.entity.Order;
import com.bibi.ui.kline.KlineActivity;
import com.bibi.ui.kline_spot.SKlineActivity;
import com.bibi.ui.my_order.OrderContract;
import com.bibi.ui.my_order.OrderFragment;
import com.bibi.utils.WonderfulToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/9/15
 */
public class HomeFragment extends BaseLazyFragment {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView; // 涨幅榜

    private int status;
    private HomeAdapter mHomeAdapter; // 首页适配器
    private List<Currency> currenciesTwo = new ArrayList<>();

    public static HomeFragment getInstance(int status) {
        HomeFragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("status", status);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        status = getArguments().getInt("status");
        initRvContent();
    }

    @Override
    protected void obtainData() {

    }

    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {

    }

    public void dataLoaded(List<Currency> currencies) {
        this.currenciesTwo.clear();
        this.currenciesTwo.addAll(currencies);
        if (mHomeAdapter != null) mHomeAdapter.notifyDataSetChanged();
    }

    private void initRvContent() {
        // 涨幅榜的适配器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mHomeAdapter = new HomeAdapter(currenciesTwo,status);
        View head = View.inflate(getActivity(), R.layout.adapter_home_head, null);
        if (status == 2) {
            TextView HomeChg = head.findViewById(R.id.item_home_chg);
            HomeChg.setText("24H成交额");
        }
        mHomeAdapter.addHeaderView(head);
        mHomeAdapter.isFirstOnly(true);
        mHomeAdapter.setLoad(true);
        mRecyclerView.setAdapter(mHomeAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mHomeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mHomeAdapter.getData().get(position).getExchangeable() == 1) {
                    SKlineActivity.actionStart(getActivity(), mHomeAdapter.getData().get(position).getSymbol());
                } else {
                    WonderfulToastUtils.showToast("暂未开放");
                }
            }
        });
    }
}
