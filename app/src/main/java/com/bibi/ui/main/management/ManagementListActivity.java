package com.bibi.ui.main.management;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bibi.entity.ProfitDetailEntity;
import com.bibi.entity.ProfitListEntity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import com.bibi.R;
import com.bibi.adapter.ManagementListAdapter;
import com.bibi.app.Injection;
import com.bibi.base.BaseActivity;
import com.bibi.ui.main.management.presenter.AssetWalletPresenter;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/8/10
 */
public class ManagementListActivity extends BaseActivity implements ManagementContract.AssetWalletView{
    @BindView(R.id.llTitle)
    RelativeLayout llTitle;
    @BindView(R.id.recycler)
    RecyclerView rvAds;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @OnClick(R.id.ibBack)
    void back(){
        finish();
    }

    private ManagementListAdapter adapter;
    private List<ProfitDetailEntity> data=new ArrayList<>();
    private int type=0;
    private ManagementContract.AssetWalletPresenter presenter;
    private int page=1;

    public static void actionStart(Context context,int type) {
        Intent intent = new Intent(context, ManagementListActivity.class);
        intent.putExtra("type",type);
        context.startActivity(intent);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_management_list;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        type = getIntent().getIntExtra("type",0);
        if(type==0){
            tvTitle.setText("收益明细");
        }else {
            tvTitle.setText("团队明细");
        }
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    @Override
    protected void obtainData() {
        presenter = new AssetWalletPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
    }

    @Override
    protected void fillWidget() {
        initRvAds();
    }

    @Override
    protected void loadData() {
        if(type==0){
//            presenter.getProfitDetail(getToken(),page);
            presenter.getStaticProfit(getToken(),page);
        }else {
            presenter.getTeamProfit(getToken(),page);
        }
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
        adapter = new ManagementListAdapter(this,R.layout.adapter_managment_list, data);
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

//    @Override
//    public void getDataSuccess(ProfitEntity entity) {
//        List<ManagementEntity> obj=entity.getDetail().getContent();
//        adapter.setEnableLoadMore(true);
//        adapter.loadMoreComplete();
//        refreshLayout.setEnabled(true);
//        refreshLayout.setRefreshing(false);
//
//        if (page==1){
//            data.clear();
//            if (obj.size() == 0) adapter.loadMoreEnd();
//            else this.data.addAll(obj);
//        }else {
//            if (obj.size() != 0) {
//                this.data.addAll(obj);
//            }else {
//                adapter.loadMoreEnd();
//            }
//        }
//
//        adapter.notifyDataSetChanged();
//        adapter.disableLoadMoreIfNotFullPage();
//    }

    @Override
    public void setPresenter(ManagementContract.AssetWalletPresenter presenter) {
        this.presenter=presenter;
    }

    private void refresh() {
        page=1;
        if(type==0){
            presenter.getStaticProfit(getToken(),page);
        }else {
            presenter.getTeamProfit(getToken(),page);
        }
        adapter.setEnableLoadMore(false);
        adapter.loadMoreEnd(false);
    }

    private void loadMore() {
        page=1+page;
        refreshLayout.setEnabled(false);
        if(type==0){
            presenter.getStaticProfit(getToken(),page);
        }else {
            presenter.getTeamProfit(getToken(),page);
        }
    }

    @Override
    public void getDataSuccess(List<ProfitListEntity> datas) {

    }

    @Override
    public void getDetailDataSuccess(List<ProfitDetailEntity> obj) {
        adapter.setEnableLoadMore(true);
        adapter.loadMoreComplete();
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);

        if (page == 1) {
            data.clear();
            if (obj.size() == 0) adapter.loadMoreEnd();
            else this.data.addAll(obj);
        } else {
            if (obj.size() != 0) {
                this.data.addAll(obj);
            } else {
                adapter.loadMoreEnd();
            }
        }

        adapter.notifyDataSetChanged();
        adapter.disableLoadMoreIfNotFullPage();
    }
}
