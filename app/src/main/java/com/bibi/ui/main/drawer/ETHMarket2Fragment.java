package com.bibi.ui.main.drawer;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.bibi.R;
import com.bibi.ui.main.MarketBaseFragment;
import com.bibi.ui.main.presenter.CommonPresenter;
import com.bibi.ui.main.presenter.ICommonView;
import com.bibi.ui.login.LoginActivity;
import com.bibi.adapter.Homes2Adapter;
import com.bibi.app.GlobalConstant;
import com.bibi.app.MyApplication;
import com.bibi.entity.Currency;
import com.bibi.utils.WonderfulCodeUtils;
import com.bibi.utils.WonderfulToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import com.bibi.app.Injection;

/**
 * Created by Administrator on 2018/1/29.
 */

public class ETHMarket2Fragment extends MarketBaseFragment implements ICommonView {

    @BindView(R.id.rvContent)
    RecyclerView rvContent;
    private Homes2Adapter adapter;
    private List<Currency> currencies = new ArrayList<>();
    private CommonPresenter commonPresenter;

    public static ETHMarket2Fragment getInstance( ) {
        ETHMarket2Fragment ethMarketFragment = new ETHMarket2Fragment();
        Bundle bundle = new Bundle();
        ethMarketFragment.setArguments(bundle);
        return ethMarketFragment;
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

    }

    @Override
    protected void fillWidget() {
        initRvContent();
    }

    private void initRvContent() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvContent.setLayoutManager(manager);
        adapter = new Homes2Adapter( currencies, 1);
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
                ((MarketOperateCallback) getActivity()).itemClick(ETHMarket2Fragment.this.adapter.getItem(position),0 );
            }
        });
        adapter.isFirstOnly(true);
        rvContent.setAdapter(adapter);
    }

    private void collectClick(int position) {
        if (!MyApplication.getApp().isLogin()) {
            WonderfulToastUtils.showToast(getResources().getString(R.string.text_xian_login));
            return;
        }
        String symbol = currencies.get(position).getSymbol();
        if (currencies.get(position).isCollect()) commonPresenter.delete(getmActivity().getToken(), symbol, position);
        else commonPresenter.add(getmActivity().getToken(), symbol, position);
    }

    @Override
    protected void loadData() {
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return false;
    }

    public void dataLoaded(List<Currency> baseEth) {
        this.currencies.clear();
        this.currencies.addAll(baseEth);
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
        this.currencies.get(position).setCollect(false);
        adapter.notifyDataSetChanged();
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
        this.currencies.get(position).setCollect(true);
        adapter.notifyDataSetChanged();
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
