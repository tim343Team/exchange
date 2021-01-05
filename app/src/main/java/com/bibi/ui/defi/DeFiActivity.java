package com.bibi.ui.defi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.bibi.R;
import com.bibi.adapter.DifiAdapter;
import com.bibi.adapter.ScoreRecordAdapter;
import com.bibi.app.Injection;
import com.bibi.base.BaseActivity;
import com.bibi.entity.Currency;
import com.bibi.entity.DifiBean;
import com.bibi.ui.kline.KlineActivity;
import com.bibi.ui.kline_spot.SKlineActivity;
import com.bibi.ui.my_promotion.PromotionRewardActivity;
import com.bibi.ui.my_promotion.PromotionRewardContract;
import com.bibi.ui.my_promotion.PromotionRewardPresenter;
import com.bibi.utils.WonderfulCodeUtils;
import com.bibi.utils.WonderfulToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/9/23
 */
public class DeFiActivity extends BaseActivity implements DeFiContract.View {
    @BindView(R.id.llTitle)
    LinearLayout llTitle;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private int page = 1;

    private DeFiContract.Presenter presenter;
    private List<Currency> rewards = new ArrayList<>();
    private DifiAdapter adapter;
    private Gson gson = new Gson();

    @OnClick(R.id.ibBack)
    public void back() {
        finish();
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, DeFiActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_difi;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        new DifiPresenter(Injection.provideTasksRepository(this.getApplicationContext()), this);
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
        presenter.getDeFiList(this.getToken(), page + "");
    }

    private void refresh() {
        page = 1;
        presenter.getDeFiList(this.getToken(), page + "");
        adapter.setEnableLoadMore(false);
        adapter.loadMoreEnd(false);
    }

    private void loadMore() {
        page = 1 + page;
        refreshLayout.setEnabled(false);
        presenter.getDeFiList(this.getToken(), page + "");
    }

    private void initRv() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(manager);
        adapter = new DifiAdapter(R.layout.adapter_difi, rewards);
        adapter.bindToRecyclerView(recycler);
        adapter.setEmptyView(R.layout.empty_no_message);
        adapter.setOnKlineListener(new DifiAdapter.OnclickListenerKline() {
            @Override
            public void onStart(String symbol, int exchangeable) {
                if (exchangeable == 1) {
                    SKlineActivity.actionStart(DeFiActivity.this, symbol);
                } else {
                    WonderfulToastUtils.showToast("暂未开放");
                }
            }
        });
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

//    @Override
//    public void getDeFiFail(Integer code, String toastMessage) {
//        adapter.setEnableLoadMore(true);
//        refreshLayout.setEnabled(true);
//        refreshLayout.setRefreshing(false);
//        WonderfulCodeUtils.checkedErrorCode(this, code, toastMessage);
//    }
//
//    @Override
//    public void getDeFiSuccess(List<DifiBean> obj) {
//        adapter.setEnableLoadMore(true);
//        adapter.loadMoreComplete();
//        refreshLayout.setEnabled(true);
//        refreshLayout.setRefreshing(false);
//
//        if (page == 1) {
//            rewards.clear();
//            if (obj.size() == 0) adapter.loadMoreEnd();
//            else this.rewards.addAll(obj);
//        } else {
//            if (obj.size() != 0) {
//                this.rewards.addAll(obj);
//            } else {
//                adapter.loadMoreEnd();
//            }
//        }
//
//        adapter.notifyDataSetChanged();
//        adapter.disableLoadMoreIfNotFullPage();
//    }

    @Override
    public void setPresenter(DeFiContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void newTickerSuccess(Object obj) {
        JsonObject object = new JsonParser().parse((String) obj).getAsJsonObject();
        try {
            JsonArray array = object.getAsJsonArray("data").getAsJsonArray();
            List<Currency> currency = gson.fromJson(array, new TypeToken<List<Currency>>() {
            }.getType());
            adapter.setEnableLoadMore(true);
            adapter.loadMoreComplete();
            refreshLayout.setEnabled(true);
            refreshLayout.setRefreshing(false);

            if (page == 1) {
                rewards.clear();
                if (currency.size() == 0) adapter.loadMoreEnd();
                else this.rewards.addAll(currency);
            } else {
                if (currency.size() != 0) {
                    this.rewards.addAll(currency);
                } else {
                    adapter.loadMoreEnd();
                }
            }

            adapter.notifyDataSetChanged();
            adapter.disableLoadMoreIfNotFullPage();
        }catch (Exception e){

        }
    }

    @Override
    public void newTickerFail(Integer code, String toastMessage) {
        adapter.setEnableLoadMore(true);
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
        WonderfulCodeUtils.checkedErrorCode(this, code, toastMessage);
    }
}
