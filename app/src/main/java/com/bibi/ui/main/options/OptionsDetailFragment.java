package com.bibi.ui.main.options;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bibi.R;
import com.bibi.adapter.HoldAdapter;
import com.bibi.adapter.OptionAdapter;
import com.bibi.app.Injection;
import com.bibi.base.BaseFragment;
import com.bibi.entity.Currency;
import com.bibi.entity.HoldEntity;
import com.bibi.entity.OptionEntity;
import com.bibi.ui.order.OrdersPresenter;
import com.bibi.ui.share.ShareActivity;
import com.bibi.utils.SharedPreferenceInstance;

import butterknife.BindView;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/6/11
 */
public class OptionsDetailFragment extends BaseFragment implements OptionsContract.DetailView {
    public static String HOLD = "HOLD";
    public static String HISTORY = "HISTORY";
    @BindView(R.id.rvAds)
    RecyclerView rvAds;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    private String type;
    private int pageNo = 1;
    private int pageSize = 40;
    private OptionAdapter adapter;
    private List<OptionEntity> data = new ArrayList<>();
    private OptionsContract.DetailPresenter presenter;

    public static OptionsDetailFragment getInstance(String type) {
        OptionsDetailFragment depthFragment = new OptionsDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        depthFragment.setArguments(bundle);
        return depthFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_options_detail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        presenter = new DetailPresenter(Injection.provideTasksRepository(getActivity().getApplicationContext()), this);
        type = getArguments().getString("type");
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (type.equals(HOLD)) {
                    refreshLayout.setRefreshing(false);
                } else {
                    refresh();
                }
            }
        });
    }

    @Override
    protected void obtainData() {
        if (type.equals(HOLD)) {
            presenter.getHold(SharedPreferenceInstance.getInstance().getTOKEN(), pageNo);
        } else if (type.equals(HISTORY)) {
            presenter.getHistory(SharedPreferenceInstance.getInstance().getTOKEN(), pageNo, "");
        }
    }

    @Override
    protected void fillWidget() {
        initRvAds();
    }

    @Override
    protected void loadData() {
//        load(type);
    }

    private void initRvAds() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvAds.setLayoutManager(manager);
        if (type.equals(HOLD)) {
            adapter = new OptionAdapter(getmActivity(), R.layout.item_option, data, 0);
        } else if (type.equals(HISTORY)) {
            adapter = new OptionAdapter(getmActivity(), R.layout.item_option_history, data, 1);
        }
        adapter.bindToRecyclerView(rvAds);
        if (type.equals(HISTORY)) {
            adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    pageNo++;
                    loadMore();
                }
            }, rvAds);
        }
        adapter.setEmptyView(R.layout.empty_rv_ad);
        adapter.setCallBackLister(new OptionAdapter.CallBackLister() {
            @Override
            public void onCallback(final OptionEntity item, final int position,String orderId) {
//                rvAds.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        data.remove(item);
//                        adapter.cancelPostionTimers(position);
//                        adapter.notifyItemRemoved(position);
//                    }
//                });
                data.remove(item);
                adapter.cancelPostionTimers(orderId);
                adapter.notifyItemRemoved(position);
            }
        });
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

    private void load(String type) {
//        if (type.equals(HOLD)) {
//            presenter.getHold(SharedPreferenceInstance.getInstance().getTOKEN(), pageNo);
//        } else
        if (type.equals(HISTORY)) {
            presenter.getHistory(SharedPreferenceInstance.getInstance().getTOKEN(), pageNo, "");
        }
    }

    @Override
    public void setPresenter(OptionsContract.DetailPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getHistorySuccess(List<OptionEntity> data) {
        adapter.loadMoreComplete();
        if (refreshLayout == null) {
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
    public void getHoldSuccess(List<OptionEntity> data) {
        adapter.loadMoreComplete();
        if (refreshLayout == null) {
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

    public void dataLoaded(List<Currency> currencie) {
        Map<String, String> priceMap = new HashMap<>();
        for (Currency currency : currencie) {
            priceMap.put(currency.getSymbol(), new DecimalFormat("#0.00").format(currency.getClose()));
        }
        adapter.setPriceMap(priceMap);
    }
}
