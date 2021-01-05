package com.bibi.ui.main.management;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import com.bibi.R;
import com.bibi.adapter.C2CAdapter;
import com.bibi.adapter.ContractDetailAdapter;
import com.bibi.app.Injection;
import com.bibi.app.MyApplication;
import com.bibi.base.BaseActivity;
import com.bibi.entity.AssetEntity;
import com.bibi.entity.ContractDetailEntity;
import com.bibi.ui.buy_or_sell.C2CBuyOrSellActivity;
import com.bibi.ui.coin.CoinActivity;
import com.bibi.ui.login.LoginActivity;
import com.bibi.ui.main.management.presenter.ContractPresenter;
import com.bibi.utils.WonderfulToastUtils;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/8/10
 */
public class ContractDetailDetailActivity extends BaseActivity implements ManagementContract.ContractView{
    @BindView(R.id.llTitle)
    RelativeLayout llTitle;
    @BindView(R.id.recycler)
    RecyclerView rvAds;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private ContractDetailAdapter adapter;
    private List<ContractDetailEntity> data=new ArrayList<>();
    private ManagementContract.ContractPresenter presenter;
    private int pageNo=1;

    @OnClick(R.id.ibBack)
    void back(){
        finish();
    }

    @OnClick(R.id.tvPositions)
    void getPositions(){

    }

    @OnClick(R.id.tvPrice)
    void getPrice(){

    }

    @OnClick(R.id.tvTime)
    void getTime(){

    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ContractDetailDetailActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_contract_detail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    @Override
    protected void obtainData() {
        presenter = new ContractPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
    }

    @Override
    protected void fillWidget() {
        initRvAds();
    }

    @Override
    protected void loadData() {
        presenter.getInvestDetail(getToken(),pageNo);
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(this, llTitle);
            isSetTitle = true;
        }
    }

    private void initRvAds() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvAds.setLayoutManager(manager);
        adapter = new ContractDetailAdapter(this,R.layout.adapter_contract_detail, data);
        adapter.bindToRecyclerView(rvAds);
        adapter.setEmptyView(R.layout.empty_rv_ad);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        }, rvAds);
        adapter.setEnableLoadMore(false);
    }

    @Override
    public void setPresenter(ManagementContract.ContractPresenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void getDataSuccess(List<ContractDetailEntity> obj) {
        adapter.setEnableLoadMore(true);
        adapter.loadMoreComplete();
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);

        if (pageNo==1){
            data.clear();
            if (obj.size() == 0) adapter.loadMoreEnd();
            else this.data.addAll(obj);
        }else {
            if (obj.size() != 0) {
                this.data.addAll(obj);
            }else {
                adapter.loadMoreEnd();
            }
        }
        adapter.notifyDataSetChanged();
        adapter.disableLoadMoreIfNotFullPage();
    }

    @Override
    public void submitSuccess(String data) {
        WonderfulToastUtils.showToast(data);
    }

    private void refresh() {
        pageNo=1;
        presenter.getInvestDetail(this.getToken(),pageNo);
        adapter.setEnableLoadMore(false);
        adapter.loadMoreEnd(false);
    }

    private void loadMore() {
        pageNo=1+pageNo;
        refreshLayout.setEnabled(false);
        presenter.getInvestDetail(this.getToken(),pageNo);
    }
}
