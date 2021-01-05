package com.bibi.ui.main.follow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import com.bibi.R;
import com.bibi.adapter.AddressAddAdapter;
import com.bibi.adapter.FollowHistoryAdapter;
import com.bibi.app.Injection;
import com.bibi.base.BaseActivity;
import com.bibi.entity.Address;
import com.bibi.entity.CoinTypeEntity;
import com.bibi.entity.Favorite;
import com.bibi.entity.FollowHistoryContent;
import com.bibi.entity.FollowHistoryEntity;
import com.bibi.entity.NiurenArrayEntity;
import com.bibi.entity.NiurenEntity;
import com.bibi.ui.kline.KlineActivity;
import com.bibi.ui.main.MainContract;
import com.bibi.utils.WonderfulCodeUtils;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/22
 */
public class FollowHistoryActivity extends BaseActivity implements MainContract.FollowView {
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    private int page = 1;

    private FollowHistoryAdapter adapter;
    private MainContract.FollowPresenter presenter;
    private List<FollowHistoryContent> followHistoryEntities = new ArrayList<>();

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, FollowHistoryActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.ibBack)
    public void back(){
        finish();
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_follow_history;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        new FollowPresenter(Injection.provideTasksRepository(getApplicationContext()), this);
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
        initRv();
    }

    @Override
    protected void loadData() {
        presenter.history(this.getToken(), page + "");
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(this, llTitle);
            isSetTitle = true;
        }
    }

    private void initRv() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        adapter = new FollowHistoryAdapter(R.layout.adapter_follow_history, followHistoryEntities);
        adapter.bindToRecyclerView(mRecyclerView);
        adapter.setEmptyView(R.layout.empty_address);
        adapter.setOnDeleteListener(new FollowHistoryAdapter.OnclickListenerDelete() {
            @Override
            public void onDelete(long followId) {
                presenter.cancel(FollowHistoryActivity.this.getToken(),followId+"");
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        }, mRecyclerView);
        adapter.setEnableLoadMore(false);
    }

    @Override
    public void setPresenter(MainContract.FollowPresenter presenter) {
        this.presenter = presenter;
    }

    private void refresh() {
        page = 1;
        presenter.history(this.getToken(), page + "");
        adapter.setEnableLoadMore(false);
        adapter.loadMoreEnd(false);
    }

    private void loadMore() {
        page = page + 1;
        refreshLayout.setEnabled(false);
        presenter.history(this.getToken(), page + "");
    }

    @Override
    public void niurenRankSuccess(NiurenArrayEntity obj) {

    }

    @Override
    public void niurenRankListSuccess(NiurenArrayEntity obj) {

    }

    @Override
    public void niurenRankFail(Integer code, String toastMessage) {

    }

    @Override
    public void CoinTypeSuccess(List<CoinTypeEntity> obj) {

    }

    @Override
    public void LeverageSuccess(List<String> obj) {

    }

    @Override
    public void HistorySuccess(List<FollowHistoryContent> obj) {
        try {
            adapter.setEnableLoadMore(true);
            adapter.loadMoreComplete();
            refreshLayout.setEnabled(true);
            refreshLayout.setRefreshing(false);
            if (page == 1) {
                followHistoryEntities.clear();
                if (obj.size() == 0) adapter.loadMoreEnd();
                else this.followHistoryEntities.addAll(obj);
            } else {
                if (obj.size() != 0) {
                    this.followHistoryEntities.addAll(obj);
                } else {
                    adapter.loadMoreEnd();
                }
            }
//        FollowHistoryContent content = new FollowHistoryContent();
//        followHistoryEntities.add(content);
            adapter.notifyDataSetChanged();
            adapter.disableLoadMoreIfNotFullPage();
        } catch (Exception e) {

        }

    }

    @Override
    public void historyFial(Integer code, String toastMessage) {
        try {
            adapter.setEnableLoadMore(true);
            refreshLayout.setEnabled(true);
            refreshLayout.setRefreshing(false);
            WonderfulCodeUtils.checkedErrorCode(this, code, toastMessage);
        }catch (Exception e){

        }
    }

    @Override
    public void cancelSuccess(String obj) {
        refresh();
    }

    @Override
    public void applySuccess(String obj) {

    }

    @Override
    public void applyFial(Integer code, String toastMessage) {

    }

    @Override
    public void addFollowSuccess(String obj) {

    }

    @Override
    public void addFollowFial(Integer code, String toastMessage) {

    }

    @Override
    public void getLockUSDTSuccess(String obj) {

    }

}
