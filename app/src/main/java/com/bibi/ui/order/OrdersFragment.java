package com.bibi.ui.order;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.bibi.R;
import com.bibi.adapter.HoldAdapter;
import com.bibi.app.Injection;
import com.bibi.app.MyApplication;
import com.bibi.base.BaseFragment;
import com.bibi.base.BaseLazyFragment;
import com.bibi.entity.AssetEntity;
import com.bibi.entity.Currency;
import com.bibi.entity.HoldEntity;
import com.bibi.ui.dialog.ModifyProfitDialogFragment;
import com.bibi.ui.share.ShareActivity;
import com.bibi.utils.SharedPreferenceInstance;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/25
 */
public class OrdersFragment extends BaseLazyFragment implements OrdersContract.OrdersView {
    public static String HOLD = "HOLD";
    public static String ENTRUST = "ENTRUST";
    public static String FINISH = "FINISH";

    @BindView(R.id.tvCloseAll)
    TextView tvCloseAll;
    @BindView(R.id.rvAds)
    RecyclerView rvAds;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    Unbinder unbinder;

    private OrdersContract.OrdersPresenter presenter;
    private String symbol;
    private String type;
    private int pageNo = 1;
    private int pageSize = 40;
    private HoldAdapter adapter;
    private List<HoldEntity> data = new ArrayList<>();
    private List<Currency> currencie=new ArrayList<>();
    String startTime = "";
    String endTime = "";

    public static OrdersFragment getInstance(String symbol, String type) {
        OrdersFragment ordersFragment = new OrdersFragment();
        Bundle bundle = new Bundle();
        bundle.putString("symbol", symbol);
        bundle.putString("type", type);
        ordersFragment.setArguments(bundle);
        return ordersFragment;
    }

    @OnClick(R.id.tvCloseAll)
    public void closeAll(){
        presenter.getCloseAll(SharedPreferenceInstance.getInstance().getTOKEN());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order_list;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        presenter = new OrdersPresenter(Injection.provideTasksRepository(getActivity().getApplicationContext()), this);
        symbol = getArguments().getString("symbol");
        type = getArguments().getString("type");
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        if(type.equals(HOLD)){
            tvCloseAll.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void obtainData() {

    }

    @Override
    protected void fillWidget() {
        initRvAds();
    }

    @Override
    protected void loadData() {
        load(type);
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

    private void initRvAds() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvAds.setLayoutManager(manager);
        if (type.equals(HOLD)) {
            adapter = new HoldAdapter(R.layout.adapter_hold, data, type,currencie);
        } else if (type.equals(ENTRUST)) {
            adapter = new HoldAdapter(R.layout.adapter_current_hold, data, type,currencie);
        } else {
            adapter = new HoldAdapter(R.layout.adapter_finish, data, type,currencie);

        }
        adapter.setOnColseListener(new HoldAdapter.OnclickListenerColse() {
            @Override
            public void close(String orderId) {
                presenter.getClose(SharedPreferenceInstance.getInstance().getTOKEN(), orderId);
            }
        });
        adapter.setOnModifyListener(new HoldAdapter.OnclickListenerModify() {
            @Override
            public void modify(String orderId, String stopProfitPrice, String stopLossPrice) {
                showBottomModify(orderId, stopProfitPrice, stopLossPrice);
            }
        });
        adapter.setOnCancleListener(new HoldAdapter.OnclickCancleListener() {
            @Override
            public void cancle(String orderId) {
                presenter.getCancle(SharedPreferenceInstance.getInstance().getTOKEN(), orderId);

            }
        });
        adapter.setOnShareListener(new HoldAdapter.OnclickListenerShare() {
            @Override
            public void share(HoldEntity item) {
                ShareActivity.actionStart(getmActivity(),item);
            }
        });
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

    @Override
    public void setPresenter(OrdersContract.OrdersPresenter presenter) {
        this.presenter = presenter;
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
    public void getHoldFinishSuccess(List<HoldEntity> data) {
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
    public void getHoldFinishFial(int e, String meg) {

    }

    @Override
    public void getHoldCurrentSuccess(List<HoldEntity> data) {
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
    public void getHoldCurrentFial(int e, String meg) {

    }

    @Override
    public void getCancleSuccess(String success) {
        refresh();
    }

    @Override
    public void getCancleFial(int e, String meg) {

    }

    @Override
    public void getCloseAllSuccess(String success) {
        refresh();
    }

    @Override
    public void getCloseSuccess(String success) {
        refresh();
    }

    @Override
    public void getCloseFial(int e, String meg) {

    }

    @Override
    public void getModifySuccess(String success) {
        refresh();
    }

    @Override
    public void allCurrencySuccess(Object obj) {

    }

    @Override
    public void getAssetPNLSuccess(AssetEntity assetEntity) {

    }

    public void dataLoaded(List<Currency> currencie) {
        this.currencie.addAll(currencie);
        adapter.setCurrencies(currencie);
        if (type.equals(HOLD)) {
            pageNo = 1;
            presenter.getHoldOrder(SharedPreferenceInstance.getInstance().getTOKEN(), pageNo, pageSize, "", "HOLD",startTime,endTime);
        } else if (type.equals(ENTRUST)) {
            pageNo = 1;
            presenter.getHoldCurrentOrder(SharedPreferenceInstance.getInstance().getTOKEN(), pageNo, pageSize, "", startTime, endTime);
        }
    }

    private void load(String type) {
        if (type.equals(HOLD)) {
            presenter.getHoldOrder(SharedPreferenceInstance.getInstance().getTOKEN(), pageNo, pageSize, "", "HOLD",startTime,endTime);
        } else if (type.equals(FINISH)) {
            presenter.getHoldFinishOrder(SharedPreferenceInstance.getInstance().getTOKEN(), pageNo, pageSize, "",startTime,endTime);
        } else if (type.equals(ENTRUST)) {
            presenter.getHoldCurrentOrder(SharedPreferenceInstance.getInstance().getTOKEN(), pageNo, pageSize, "",startTime,endTime);
        }

    }

    private void showBottomModify(String orderId, String stopProfitPrice, String stopLossPrice) {
        ModifyProfitDialogFragment modifyProfitFragment = ModifyProfitDialogFragment.getInstance(orderId, stopProfitPrice, stopLossPrice);
        modifyProfitFragment.show(getActivity().getSupportFragmentManager(), "bottom");
        modifyProfitFragment.setCallback(new ModifyProfitDialogFragment.OperateCallback() {
            @Override
            public void cancleOrder(String orderId, String stopProfitPrice, String stopLossPrice) {
                presenter.getModifyProfit(SharedPreferenceInstance.getInstance().getTOKEN(), orderId, stopProfitPrice, stopLossPrice);
            }
        });
    }

    public void dataLoaded(String startTime, String endTime) {
            this.startTime=startTime;
            this.endTime=endTime;
            refresh();
    }
}
