package com.bibi.ui.coin;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bibi.base.BaseFragment;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import com.bibi.R;
import com.bibi.adapter.CoinHoldAdapter;
import com.bibi.adapter.HoldAdapter;
import com.bibi.app.Injection;
import com.bibi.base.BaseLazyFragment;
import com.bibi.entity.Currency;
import com.bibi.entity.HoldEntity;
import com.bibi.ui.order.OrdersContract;
import com.bibi.ui.order.OrdersFragment;
import com.bibi.ui.order.OrdersPresenter;
import com.bibi.ui.share.ShareActivity;
import com.bibi.utils.SharedPreferenceInstance;
import com.bibi.utils.WonderfulToastUtils;
import butterknife.BindView;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/7/27
 */
public class CoinsFragment extends BaseFragment implements CoinsContract.CoinsView {
    public static String HOLD = "HOLD";
    public static String FINISH = "FINISH";

    @BindView(R.id.rvAds)
    RecyclerView rvAds;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private CoinsContract.Presenter presenter;
    private String symbol;
    private String type;
    private int pageNo = 1;
    private int pageSize = 15;
    private CoinHoldAdapter adapter;
    private List<HoldEntity> data = new ArrayList<>();
    private List<Currency> currencie = new ArrayList<>();

    public static CoinsFragment getInstance(String symbol, String type) {
        CoinsFragment ordersFragment = new CoinsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("symbol", symbol);
        bundle.putString("type", type);
        ordersFragment.setArguments(bundle);
        return ordersFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_coins_list;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        presenter = new CoinsPresenter(Injection.provideTasksRepository(getActivity().getApplicationContext()), this);
        symbol = getArguments().getString("symbol");
        type = getArguments().getString("type");
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    @Override
    protected void obtainData() {

    }

    @Override
    protected void fillWidget() {
        initRvAds();
        load(type);
    }

    @Override
    protected void loadData() {
    }

    private void loadMore() {
        refreshLayout.setEnabled(false);
        load(type);
    }

    private void refresh() {
        pageNo = 1;
        load(type);
        adapter.setEnableLoadMore(true);
        adapter.loadMoreEnd(false);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(CoinsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private void initRvAds() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvAds.setLayoutManager(manager);
        if (type.equals(HOLD)) {
            adapter = new CoinHoldAdapter(R.layout.adapter_coin_2_coin, data, type,currencie);
            adapter.setOnColseListener(new CoinHoldAdapter.OnclickListenerColse() {
                @Override
                public void close(String orderId) {
                    presenter.cancle(SharedPreferenceInstance.getInstance().getTOKEN(),orderId);
                }
            });
        }  else {
            adapter = new CoinHoldAdapter(R.layout.adapter_coin_2_coin, data, type,currencie);
        }
        adapter.bindToRecyclerView(rvAds);
        adapter.isFirstOnly(true);
        if(type.equals("FINISH")) {
            adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    pageNo++;
                    loadMore();
                }
            }, rvAds);
        }
        adapter.setEmptyView(R.layout.empty_rv_ad);
    }

    private void load(String type) {
        if (type.equals(HOLD)) {
            presenter.getHoldOrder(SharedPreferenceInstance.getInstance().getTOKEN(), pageNo, pageSize,"","");
        } else if (type.equals(FINISH)) {
            presenter.getHoldFinishOrder(SharedPreferenceInstance.getInstance().getTOKEN(), pageNo, pageSize,"","");
        }
    }

    @Override
    public void errorMes(int e, String meg) {

    }

    @Override
    public void getHoldSuccess(List<HoldEntity> data) {
        adapter.loadMoreComplete();
        if (refreshLayout==null){
            return;
        }
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);

        if (data == null || data.size() == 0) {
            if (pageNo == 1) {
                this.data.clear();
                adapter.notifyDataSetChanged();
            }
            return;
        }
        if (pageNo == 1) {
            this.data.clear();
            this.data.addAll(data);
        } else {
            this.data.addAll(data);
        }
        if (data.size() < pageSize) {
            adapter.loadMoreEnd();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getHoldFial(int e, String meg) {

    }

    @Override
    public void getCancleSuccess(String success) {
        WonderfulToastUtils.showToast(success);
        refresh();
    }

    @Override
    public void setSpotCurrency(List<Currency> obj) {

    }

    public void dataLoaded(List<Currency> currencie) {
        this.currencie.addAll(currencie);
        adapter.setCurrencies(currencie);
        if (type.equals(HOLD)) {
            pageNo = 1;
            presenter.getHoldOrder(SharedPreferenceInstance.getInstance().getTOKEN(), pageNo, pageSize, "", "");
        }
    }
}
