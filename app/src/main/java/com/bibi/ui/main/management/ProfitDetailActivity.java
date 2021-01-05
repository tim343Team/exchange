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
import com.bibi.adapter.ContractDetailAdapter;
import com.bibi.adapter.ProfitDetailAdapter;
import com.bibi.app.Injection;
import com.bibi.base.BaseActivity;
import com.bibi.entity.ContractDetailEntity;
import com.bibi.entity.InvestProfitEntity;
import com.bibi.entity.ProfitDetailEntity;
import com.bibi.ui.main.management.presenter.ContractPresenter;
import com.bibi.ui.main.management.presenter.ProfitPresenter;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/8/10
 */
public class ProfitDetailActivity extends BaseActivity implements ManagementContract.ProfitView {
    @BindView(R.id.llTitle)
    RelativeLayout llTitle;
    @BindView(R.id.recycler)
    RecyclerView rvAds;
    @BindView(R.id.tvAllProfit)
    TextView tvAllProfit;
    @BindView(R.id.tvTadyProfit)
    TextView tvTadyProfit;
    @BindView(R.id.tvAllCost)
    TextView tvAllCost;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindViews({R.id.tvTab1, R.id.tvTab2, R.id.tvTab3, R.id.tvTab4, R.id.tvTab5})
    TextView[] tabText;
    @BindViews({R.id.vTab1, R.id.vTab2, R.id.vTab3, R.id.vTab4, R.id.vTab5})
    View[] tabView;

    private int pageNo = 1;
    private ManagementContract.ProfitPresenter presenter;
    private ProfitDetailAdapter adapter;
    private List<ProfitDetailEntity> data = new ArrayList<>();
    private String type = "static";//"static" /"invite" /"spot" /"team" /"leader"


    @OnClick(R.id.ibBack)
    void back() {
        finish();
    }

    @OnClick(R.id.llTab1)
    void selectTab1() {
        pageNo = 1;
        type = "static";
        selectTab(0);
        data.clear();
        adapter.notifyDataSetChanged();
        presenter.getInvestProfitDetail(getToken(), type, pageNo);
    }

    @OnClick(R.id.llTab2)
    void selectTab2() {
        pageNo = 1;
        type = "invite";
        selectTab(1);
        data.clear();
        adapter.notifyDataSetChanged();
        presenter.getInvestProfitDetail(getToken(), type, pageNo);
    }

    @OnClick(R.id.llTab3)
    void selectTab3() {
        pageNo = 1;
        type = "spot";
        selectTab(2);
        data.clear();
        adapter.notifyDataSetChanged();
        presenter.getInvestProfitDetail(getToken(), type, pageNo);
    }

    @OnClick(R.id.llTab4)
    void selectTab4() {
        pageNo = 1;
        type = "team";
        selectTab(3);
        data.clear();
        adapter.notifyDataSetChanged();
        presenter.getInvestProfitDetail(getToken(), type, pageNo);
    }

    @OnClick(R.id.llTab5)
    void selectTab5() {
        pageNo = 1;
        type = "leader";
        selectTab(4);
        data.clear();
        adapter.notifyDataSetChanged();
        presenter.getInvestProfitDetail(getToken(), type, pageNo);
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ProfitDetailActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_profit_detail;
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
        presenter = new ProfitPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
    }

    @Override
    protected void fillWidget() {
        initRvAds();
    }

    @Override
    protected void loadData() {
        presenter.getInvestProfit(getToken());
        presenter.getInvestProfitDetail(getToken(), type, pageNo);
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
        adapter = new ProfitDetailAdapter(this, R.layout.adapter_profit_detail, data);
        View head = View.inflate(this, R.layout.adapter_profit_header, null);
        adapter.addHeaderView(head);
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
    public void getDataDetailSuccess(List<ProfitDetailEntity> obj) {
        adapter.setEnableLoadMore(true);
        adapter.loadMoreComplete();
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);

        if (pageNo == 1) {
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

    @Override
    public void getDataSuccess(List<InvestProfitEntity> datas) {
        double totalProfit = 0.0;
        double todayProfit = 0.0;
        double totalInvest = 0.0;
        for (InvestProfitEntity entity : datas) {
            totalProfit += entity.getTotalProfit();
            todayProfit += entity.getTodayProfit();
            totalInvest += entity.getTotalInvest();
        }
        tvAllProfit.setText(totalProfit + "");
        tvTadyProfit.setText(todayProfit + "");
        tvAllCost.setText(totalInvest + "");
    }

    @Override
    public void setPresenter(ManagementContract.ProfitPresenter presenter) {
        this.presenter = presenter;
    }

    private void refresh() {
        pageNo = 1;
        presenter.getInvestProfitDetail(getToken(), type, pageNo);
        adapter.setEnableLoadMore(false);
        adapter.loadMoreEnd(false);
    }

    private void loadMore() {
        pageNo = 1 + pageNo;
        refreshLayout.setEnabled(false);
        presenter.getInvestProfitDetail(getToken(), type, pageNo);
    }

    private void selectTab(int position) {
        for (int i = 0; i < tabText.length; i++) {
            if (i == position) {
                tabText[i].setTextColor(getResources().getColor(R.color.white));
                tabView[i].setVisibility(View.VISIBLE);
            } else {
                tabText[i].setTextColor(getResources().getColor(R.color.primaryTextGray));
                tabView[i].setVisibility(View.INVISIBLE);
            }
        }
    }
}
