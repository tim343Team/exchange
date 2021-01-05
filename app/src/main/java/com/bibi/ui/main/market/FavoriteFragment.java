package com.bibi.ui.main.market;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bibi.ui.kline_spot.SKlineActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.bibi.R;
import com.bibi.ui.kline.KlineActivity;
import com.bibi.ui.main.MarketBaseFragment;
import com.bibi.ui.main.presenter.CommonPresenter;
import com.bibi.ui.main.presenter.ICommonView;
import com.bibi.ui.login.LoginActivity;
import com.bibi.adapter.HomesAdapter;
import com.bibi.app.GlobalConstant;
import com.bibi.app.MyApplication;
import com.bibi.entity.Currency;
import com.bibi.utils.WonderfulCodeUtils;
import com.bibi.utils.WonderfulLogUtils;
import com.bibi.utils.WonderfulToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import com.bibi.app.Injection;

/**
 * Created by Administrator on 2018/1/29.
 */

public class FavoriteFragment extends MarketBaseFragment implements ICommonView {

    @BindView(R.id.rvContent)
    RecyclerView rvContent;
    private HomesAdapter adapter;
    private List<Currency> currencies = new ArrayList<>();
    private List<Currency> favorites = new ArrayList<>();
    private CommonPresenter commonPresenter;

    public static FavoriteFragment getInstance() {
        FavoriteFragment favoriteFragment = new FavoriteFragment();
        Bundle bundle = new Bundle();
        favoriteFragment.setArguments(bundle);
        return favoriteFragment;
    }

    public void isChange(boolean isLoad) {
        if (adapter != null) {
            adapter.setLoad(isLoad);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_eth_market;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        new CommonPresenter(Injection.provideTasksRepository(getActivity().getApplicationContext()), this);
    }

    @Override
    protected void obtainData() {
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    @Override
    protected void fillWidget() {
        initRvContent();
    }

    private void initRvContent() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvContent.setLayoutManager(manager);
        adapter = new HomesAdapter(favorites, 2);
//        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
//        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                collectClick(position);
//            }
//        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (favorites.get(position).getExchangeable() == 1) {
                    SKlineActivity.actionStart(getActivity(), favorites.get(position).getSymbol());
                } else {
                    WonderfulToastUtils.showToast("暂未开放");
                }
            }
        });
        adapter.isFirstOnly(true);
        rvContent.setAdapter(adapter);
        View head = View.inflate(getActivity(), R.layout.adapter_home_head, null);
        adapter.setHeaderView(head);
    }

    private void collectClick(int position) {
        if (!MyApplication.getApp().isLogin()) {
            WonderfulToastUtils.showToast(getResources().getString(R.string.text_xian_login));
            return;
        }
        String symbol = favorites.get(position).getSymbol();
        if (favorites.get(position).isCollect())
            commonPresenter.delete(getmActivity().getToken(), symbol, position);
        else commonPresenter.add(getmActivity().getToken(), symbol, position);
    }

    @Override
    protected void loadData() {
        notifyData();
    }

    private void notifyData() {
        if (adapter == null) return;
        this.favorites.clear();
        for (Currency currency : currencies) {
            if (currency.isCollect()) this.favorites.add(currency);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return false;
    }

    public void dataLoaded(List<Currency> currencies) {
        this.currencies.clear();
        this.currencies.addAll(currencies);
        this.favorites.clear();
        for (Currency currency : currencies) {
            if (currency.isCollect()) this.favorites.add(currency);
        }
//        Log.d("jiejie","234---" + favorites.size());
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(CommonPresenter presenter) {
        this.commonPresenter = presenter;
    }

    @Override
    public void deleteFail(Integer code, String toastMessage) {
        if (code == GlobalConstant.TOKEN_DISABLE1) LoginActivity.actionStart(getActivity());
        else WonderfulCodeUtils.checkedErrorCode(getmActivity(), code, toastMessage);
    }

    @Override
    public void deleteSuccess(String obj, int position) {
        this.favorites.get(position).setCollect(false);
        notifyData();
    }

    @Override
    public void addFail(Integer code, String toastMessage) {
        if (code == GlobalConstant.TOKEN_DISABLE1) LoginActivity.actionStart(getActivity());
        else WonderfulCodeUtils.checkedErrorCode(getmActivity(), code, toastMessage);
    }

    @Override
    public void getSymbolSuccess(List<Currency> cirrency) {

    }

    @Override
    public void addSuccess(String obj, int position) {
        this.favorites.get(position).setCollect(true);
        notifyData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getActivity().getSupportFragmentManager().putFragment(outState, "USDT", this);
    }

    public void tcpNotify() {
        if (getUserVisibleHint() && adapter != null) adapter.notifyDataSetChanged();
    }
}
