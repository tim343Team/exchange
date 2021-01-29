package com.bibi.ui.main.options;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bibi.R;
import com.bibi.adapter.OptionAdapter;
import com.bibi.adapter.OptionOrderAdapter;
import com.bibi.app.Injection;
import com.bibi.base.BaseFragment;
import com.bibi.entity.OptionEntity;
import com.bibi.utils.SharedPreferenceInstance;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ${description}
 *
 * @author weiqiliu
 * @version 1.0 2020/6/11
 */
public class OptionOrderFragment extends BaseFragment implements OptionsContract.View {
    @BindView(R.id.rvDepth)
    RecyclerView recyclerView;
    private String symbol = null;

    private OptionAdapter adapter;
    private List<OptionEntity> data = new ArrayList<>();
    private OptionsContract.Presenter presenter;

    @OnClick(R.id.tvMoreOrder)
    void start() {
        OptionsActivity.actionStart(getmActivity());
    }

    public static OptionOrderFragment getInstance(String symbol) {
        OptionOrderFragment depthFragment = new OptionOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putString("symbol", symbol);
        depthFragment.setArguments(bundle);
        return depthFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_option_orderr;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        presenter = new Presenter(Injection.provideTasksRepository(getActivity().getApplicationContext()), this);
        intLv();
        recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    protected void obtainData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            symbol = bundle.getString("symbol");
            presenter.getCurrentOrder(SharedPreferenceInstance.getInstance().getTOKEN(), 1, symbol);
        }
    }

    @Override
    protected void fillWidget() {

    }

    @Override
    protected void loadData() {

    }

    /**
     * 初始化列表数据
     */
    private void intLv() {
        LinearLayoutManager manager = new LinearLayoutManager(getmActivity(), LinearLayoutManager.VERTICAL, false);
        manager.setStackFromEnd(false);
        recyclerView.setFocusableInTouchMode(false);
        recyclerView.setFocusable(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        adapter = new OptionAdapter(getmActivity(), R.layout.item_option, data, 0);
        adapter.bindToRecyclerView(recyclerView);
        adapter.setEmptyView(R.layout.empty_no_order);
        adapter.setCallBackLister(new OptionAdapter.CallBackLister() {
            @Override
            public void onCallback(final OptionEntity item, final int position, final String orderId) {
                if (recyclerView != null) {
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            data.remove(item);
                            adapter.cancelPostionTimers(orderId);
                            adapter.notifyItemRemoved(position);
                        }
                    });
                } else {
                    data.remove(item);
                    adapter.cancelPostionTimers(orderId);
                    adapter.notifyItemRemoved(position);
                }
            }
        });
    }

    public void setCurrentPrice(String price) {
        if (adapter != null) {
//            adapter.setPrice(price);
            Map<String, String> priceMap = new HashMap<>();
            priceMap.put(symbol, price);
            adapter.setPriceMap(priceMap);
        }
    }

    @Override
    public void setPresenter(OptionsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getCurrentSuccess(List<OptionEntity> message) {
        adapter.cancelAllTimers();
        data.clear();
        data.addAll(message);
        adapter.notifyDataSetChanged();
    }

    void refresh() {
        if (presenter != null && symbol != null) {
            presenter.getCurrentOrder(SharedPreferenceInstance.getInstance().getTOKEN(), 1, symbol);
        }
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
        if (presenter == null) {
            return;
        }
        presenter.getCurrentOrder(SharedPreferenceInstance.getInstance().getTOKEN(), 1, symbol);
    }
}
