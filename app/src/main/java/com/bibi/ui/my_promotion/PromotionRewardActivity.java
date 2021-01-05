package com.bibi.ui.my_promotion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import com.bibi.R;
import com.bibi.adapter.ScoreRecordAdapter;
import com.bibi.app.Injection;
import com.bibi.base.BaseActivity;
import com.bibi.entity.ScoreRecordBean;
import com.bibi.utils.WonderfulCodeUtils;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/3/17
 */
public class PromotionRewardActivity  extends BaseActivity implements PromotionRewardContract.View {
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.rvPromotionReward)
    RecyclerView rvPromotionReward;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private int page=1;

    private PromotionRewardContract.Presenter presenter;
    private List<ScoreRecordBean> rewards = new ArrayList<>();
    private ScoreRecordAdapter adapter;

    @OnClick(R.id.ibBack)
    public void back(){
        finish();
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PromotionRewardActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_promotion_reward;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        new PromotionRewardPresenter(Injection.provideTasksRepository(this.getApplicationContext()), this);
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
        presenter.getPromotionReward(this.getToken(),page+"","20");
    }

    @Override
    public void getPromotionRewardFail(Integer code, String toastMessage) {
        adapter.setEnableLoadMore(true);
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        WonderfulCodeUtils.checkedErrorCode(this, code, toastMessage);
    }

    @Override
    public void getPromotionRewardSuccess(List<ScoreRecordBean> obj) {
        adapter.setEnableLoadMore(true);
        adapter.loadMoreComplete();
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);

        if (page==1){
            rewards.clear();
            if (obj.size() == 0) adapter.loadMoreEnd();
            else this.rewards.addAll(obj);
        }else {
            if (obj.size() != 0) {
                this.rewards.addAll(obj);
            }else {
                adapter.loadMoreEnd();
            }
        }

        adapter.notifyDataSetChanged();
        adapter.disableLoadMoreIfNotFullPage();
    }

    @Override
    public void setPresenter(PromotionRewardContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private void refresh() {
        page=1;
        presenter.getPromotionReward(this.getToken(),page+"","20");
        adapter.setEnableLoadMore(false);
        adapter.loadMoreEnd(false);
    }

    private void loadMore() {
        page=1+page;
        refreshLayout.setEnabled(false);
        presenter.getPromotionReward(this.getToken(),page+"","20");
    }

    private void initRv() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvPromotionReward.setLayoutManager(manager);
        adapter = new ScoreRecordAdapter(R.layout.adapter_score_record, rewards);
        adapter.bindToRecyclerView(rvPromotionReward);
        adapter.setEmptyView(R.layout.empty_no_message);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadMore();
            }
        }, rvPromotionReward);
        adapter.setEnableLoadMore(false);
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        if (!isSetTitle) {
            ImmersionBar.setTitleBar(this, llTitle);
            isSetTitle = true;
        }
    }
}
